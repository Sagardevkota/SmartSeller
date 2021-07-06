package com.example.smartseller.ui.home.AddProductFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
;

import com.example.smartseller.R;

import com.example.smartseller.data.model.Products;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.FragmentAddProductSecondPageBinding;
import com.example.smartseller.util.session.Session;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class addProductSecondPage extends Fragment {


    private static final String TAG = "ADD_PRODUCT_SECOND_PAGE";
    private FragmentAddProductSecondPageBinding binding;
    private String selectedCategory = "";
    private String selectedType = "";
    private Session session;
    private final Set<String> colorSet = new HashSet<>();
    private final Set<String> sizeSet = new HashSet<>();



    public addProductSecondPage() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddProductSecondPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        session = new Session(getContext());
        binding.tvNext.setOnClickListener(view1 -> passValues());
        binding.tvPrevious.setOnClickListener(view12 -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new addProduct()).commit());
        setSpinners();
        setAttributes();
        return view;
    }

    private void setSpinners() {
        //category spinner
        List<String> categorylist = Arrays.asList(getResources().getStringArray(R.array.category));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categorylist);
        binding.spCategory.setAdapter(arrayAdapter);
        binding.spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedCategory = categorylist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //type spinner

        List<String> typelist = Arrays.asList(getResources().getStringArray(R.array.Type));
        ArrayAdapter<String> arraytypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, typelist);
        binding.spType.setAdapter(arraytypeAdapter);
        binding.spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedType = typelist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void passValues() {
        if (validateBrandtName() && validateDiscount() && validateStock() && validateSku()) {

            //getting arguments from previous fragment
            Products products = getArguments().getParcelable("product");

            //getting values from text views
            String brandName = binding.etBrand.getText().toString().trim();
            String discount = binding.etDiscount.getText().toString().trim();
            String stock = binding.etStock.getText().toString().trim();
            String sku = binding.etSku.getText().toString().trim();

            //now set brand name, discount, stock and sku in this fragment
            products.setBrand(brandName);
            products.setDiscount(Integer.valueOf(discount));
            products.setStock(Integer.valueOf(stock));
            products.setSku(sku);
            products.setCategory(selectedCategory);
            products.setType(selectedType);
            products.setSizes(new ArrayList<>(sizeSet));
            products.setColors(new ArrayList<>(colorSet));


            //passing argument to another fragment
            postProduct(products);

        } else
            Toasty.error(getContext(), "Please fill up everything").show();


    }
    private void postProduct(Products products) {

        File file = new File(products.getPicturePath());
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Log.i(TAG, "postProduct: "+products.toString());


        showProgressDialog("Adding Product");

        SmartAPI.getApiService().addProduct(session.getJWT(), products,body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonResponse -> {
                    if (jsonResponse.getStatus().equalsIgnoreCase("200 OK")) {

                        Toasty.success(getContext(),jsonResponse.getMessage(),Toasty.LENGTH_SHORT).show();
                        hideProgressDialog();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container,new addProduct())
                                .commit();

                    } else
                        Toasty.error(getContext(), jsonResponse.getMessage()).show();
                }, throwable -> Log.e(TAG, "postProduct: " + throwable.getMessage()));

    }



    //for setting color sizes in sets and implementing listeners
    private void setAttributes() {

        binding.tvAddColors.setOnClickListener(view -> {
            //create edit text to get input from user for color
            final EditText edittext = new EditText(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMarginStart(8);
            edittext.setLayoutParams(lp);
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setView(edittext);
            builder.setTitle("Enter color")
                    .setMessage("Please enter color for your product")
                    .setPositiveButton("Enter", (dialogInterface, i) -> {

                        String addedColor = edittext.getText().toString();
                        if (colorSet.stream()
                                .anyMatch(color ->
                                        color.equalsIgnoreCase(addedColor)))
                        {
                            Toasty.error(getContext(),addedColor+" already exists",Toasty.LENGTH_SHORT).show();
                            return;
                        }
                        colorSet.add(addedColor);
                        binding.colorChipGroup.addView(createChip(addedColor,binding.colorChipGroup));
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        });


        binding.tvAddSizes.setOnClickListener(view -> {
            final EditText edittext = new EditText(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMarginStart(8);
            edittext.setLayoutParams(lp);
            edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setView(edittext);
            builder.setTitle("Enter size")
                    .setMessage("Please enter size for your product")
                    .setPositiveButton("Enter", (dialogInterface, i) -> {

                        String addedSize = edittext.getText().toString();
                        if (sizeSet.stream()
                                .anyMatch(size ->
                                        size.equalsIgnoreCase(addedSize)))
                        {
                            Toasty.error(getContext(),addedSize+" already exists",Toasty.LENGTH_SHORT).show();
                            return;
                        }

                        sizeSet.add(addedSize);
                        binding.sizeChipGroup.addView(createChip(addedSize,binding.sizeChipGroup));
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
        });


        binding.colorChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            if (chip != null) {
                String color = chip.getText().toString();

                Toasty.normal(getContext(), "Removed " + color, Toasty.LENGTH_SHORT).show();
                binding.colorChipGroup.removeView(chip);
                colorSet.removeIf(colorInSet -> colorInSet.equalsIgnoreCase(color));
            }

        });
        binding.sizeChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            if (chip != null) {
                String size = chip.getText().toString();
                Toasty.normal(getContext(), "Removed " + size, Toasty.LENGTH_SHORT).show();
                binding.sizeChipGroup.removeView(chip);
                sizeSet.removeIf(sizeInSet -> sizeInSet.equalsIgnoreCase(size));
            }

        });
    }





    private Chip createChip(String text, ChipGroup viewGroup) {
        Chip chip =
                (Chip) getLayoutInflater()
                        .inflate(R.layout.single_chip_layout, viewGroup, false);
        chip.setText(text);
        return chip;
    }

    private boolean validateBrandtName() {
        return binding.etBrand.getText().length() != 0;

    }

    private boolean validateDiscount() {
        return binding.etDiscount.getText().length() != 0;

    }

    private boolean validateStock() {
        return binding.etStock.getText().length() != 0;
    }

    private boolean validateSku() {
        return binding.etSku.getText().length() != 0;
    }

    private static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }


    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(msg);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}