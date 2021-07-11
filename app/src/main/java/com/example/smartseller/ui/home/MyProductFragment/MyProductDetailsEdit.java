package com.example.smartseller.ui.home.MyProductFragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.VisibleForTesting;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.smartseller.R;
import com.example.smartseller.data.model.Products;
import com.example.smartseller.data.network.JsonResponse;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.FragmentMyProductDetailsEditBinding;
import com.example.smartseller.util.session.Session;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;


public class MyProductDetailsEdit extends Fragment {
    private static final String TAG = "MY_PRODUCT_DETAILS_EDIT";
    private FragmentMyProductDetailsEditBinding binding;
    private final int GALLERY_REQUEST = 10002;
    private String imgPath = ""; //local image path to create request body and multipart file
    private String picturePath = "";
    private Session session;
    Integer productId;
    private String categoryl;
    private String type;
    private final Set<String> selectedColorAttributeList = new HashSet<>();
    private final Set<String> selectedSizeAttributeList = new HashSet<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyProductDetailsEditBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        session = new Session(getContext());
        getPassedValues();
        setSpinners();
        binding.ivEdit.setOnClickListener(view1 -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        });
        binding.btnSave.setOnClickListener(view12 -> getAndPutValues());


        return view;
    }

    private void getAndPutValues() {

        showProgressDialog("Updating Product");
        String product_name = binding.etProductName.getText().toString().trim();
        String description = binding.etDesc.getText().toString().trim();
        String discountStr = binding.etDiscount.getText().toString().trim();
        Integer discount = Integer.valueOf(discountStr);
        String category = categoryl;
        String brand = binding.etBrand.getText().toString().trim();

        String price = binding.etPrice.getText().toString().trim();
        String stockstr = binding.etStock.getText().toString().trim();
        Integer stock = Integer.valueOf(stockstr);
        String sku = binding.etSku.getText().toString().trim();
        Integer sellerId = session.getUserId();

        Products products = Products.builder()
                .productId(productId)
                .productName(product_name)
                .desc(description)
                .price(price)
                .category(category)
                .brand(brand)
                .sku(sku)
                .type(type)
                .picturePath(picturePath)
                .discount(discount)
                .stock(stock)
                .seller_id(sellerId)
                .colors(new ArrayList<>(selectedColorAttributeList))
                .sizes(new ArrayList<>(selectedSizeAttributeList))
                .build();

        //if user chooses from gallery path of file in internal sd is not null
        if (imgPath.length()!=0){
            requestPermission();
            File file = new File(imgPath);
             RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
           MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);

             SmartAPI.getApiService().updateProduct(session.getJWT(), products,body)
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(response -> {
                         if (response.getStatus().equalsIgnoreCase("200 OK")){
                             Toasty.success(getContext(), response.getMessage()).show();
                             hideProgressDialog();
                         }
                         else {Toasty.error(getContext(), response.getMessage()).show();
                         hideProgressDialog();
                         }
                     },throwable -> Log.e(TAG, "getAndPutValues: "+throwable.getMessage() ));


        }

        else
            SmartAPI.getApiService().updateProductWithoutImage(session.getJWT(),products)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(response -> {
                if (response.getStatus().equalsIgnoreCase("200 OK")){
                    Toasty.success(getContext(), response.getMessage()).show();
                    hideProgressDialog();
                }
                else {Toasty.error(getContext(), response.getMessage()).show();
                hideProgressDialog();
                }
            },throwable -> Log.e(TAG, "getAndPutValues: "+throwable.getMessage() ));



        Log.i(TAG, "getAndPutValues: "+products);

    }

    @Override
    public void onStart() {
        super.onStart();
        requestPermission();
    }

    private void getPassedValues() {
        Bundle b = getArguments();
        Products products = b.getParcelable("product");
        Toasty.success(getContext(), products.getProductName()).show();
        binding.etProductName.setText(products.getProductName());
        binding.etDesc.setText(products.getDesc());
        binding.etPrice.setText(String.valueOf(products.getPrice()));
        binding.etBrand.setText(products.getBrand());

        binding.etSku.setText(products.getSku());
        binding.etDiscount.setText(String.valueOf(products.getDiscount()));
        binding.etStock.setText(String.valueOf(products.getStock()));


        productId = products.getProductId();
        picturePath = products.getPicturePath();

        //if there is no color at all
        if (products.getColors().size()!=0){
            selectedColorAttributeList.addAll(products.getColors());

            //get all color from set and create each chip
            selectedColorAttributeList.forEach(color ->
                    binding.colorChipGroup.addView(createChip(color,
                            binding.colorChipGroup)));
        }

        //if there is no color at all
        if (products.getSizes().size()!=0){
            selectedSizeAttributeList.addAll(products.getSizes());

            //get all color from set and create each chip
            selectedSizeAttributeList.forEach(size ->
                    binding.sizeChipGroup.addView(createChip(size,
                            binding.sizeChipGroup)));
        }



        try {
            String url = SmartAPI.IMG_BASE_URL+products.getPicturePath();
            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(binding.ivProductImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Load", e.getMessage());
                        }
                    });
        } catch (Exception e) {
            Log.d("error", e.getMessage());
        }

        binding.tvEnterColor.setOnClickListener(view -> {
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
                        if (selectedColorAttributeList.stream()
                                .anyMatch(color ->
                                        color.equalsIgnoreCase(addedColor)))
                        {
                            Toasty.error(getContext(),addedColor+" already exists",Toasty.LENGTH_SHORT).show();
                            return;
                        }
                        selectedColorAttributeList.add(addedColor);
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


        binding.tvEnterSize.setOnClickListener(view -> {
            final EditText edittext = new EditText(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMarginStart(8);
            edittext.setLayoutParams(lp);
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setView(edittext);
            builder.setTitle("Enter size")
                    .setMessage("Please enter size for your product")
                    .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String addedSize = edittext.getText().toString();
                            if (selectedSizeAttributeList.stream()
                                    .anyMatch(size ->
                                            size.equalsIgnoreCase(addedSize)))
                            {
                                Toasty.error(getContext(),addedSize+" already exists",Toasty.LENGTH_SHORT).show();
                                return;
                            }

                            selectedSizeAttributeList.add(addedSize);
                            binding.sizeChipGroup.addView(createChip(addedSize,binding.sizeChipGroup));
                        }
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
        });


        binding.colorChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            if (chip != null) {
                String color = chip.getText().toString();

                Toasty.normal(getContext(),"Removed "+color,Toasty.LENGTH_SHORT).show();
                binding.colorChipGroup.removeView(chip);
                selectedColorAttributeList.removeIf(colorInSet-> colorInSet.equalsIgnoreCase(color));
            }

        });
        binding.sizeChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            if (chip != null) {
                String size = chip.getText().toString();
                Toasty.normal(getContext(),"Removed "+size,Toasty.LENGTH_SHORT).show();
                binding.sizeChipGroup.removeView(chip);
                selectedSizeAttributeList.removeIf(sizeInSet -> sizeInSet.equalsIgnoreCase(size));
            }

        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        binding.ivProductImage.setImageBitmap(imageBitmap);
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        imgPath = picturePath;


                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
    }

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        }
        else binding.btnSave.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }




    private void setSpinners() {
        //category spinner
        List<String> categorylist = Arrays.asList(getResources().getStringArray(R.array.category));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categorylist);
        binding.spCategory.setAdapter(arrayAdapter);
        int positionOfSelectedCategory = 0;
        Products products = getArguments().getParcelable("product");
        String selectedCategory = products.getCategory();
        for (String category : categorylist) {
            if (category.equalsIgnoreCase(selectedCategory))
                break;
            positionOfSelectedCategory++;
        }


        //to make spinner select previously set category
        binding.spCategory.setSelection(positionOfSelectedCategory, true);

        categoryl = selectedCategory;


        binding.spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                categoryl = categorylist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //type spinner
        List<String> typelist = Arrays.asList(getResources().getStringArray(R.array.Type));
        ArrayAdapter<String> arraytypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, typelist);
        binding.spType.setAdapter(arraytypeAdapter);
        int positionOfSelectedType = 0;
        String selectedType = products.getType();
        for (String type : typelist) {
            if (type.equalsIgnoreCase(selectedType))
                break;
            positionOfSelectedType++;
        }

        binding.spType.setSelection(positionOfSelectedType, true);

        //select already set type
        type = selectedType;

        binding.spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                type = typelist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }



    private Chip createChip(String text, ChipGroup viewGroup){
        Chip chip =
                (Chip) getLayoutInflater()
                        .inflate(R.layout.single_chip_layout, viewGroup, false);
        chip.setText(text);
        return chip;
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