package com.example.smartseller.ui.home.AddProductFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
;

import com.example.smartseller.R;

import com.example.smartseller.databinding.FragmentAddProductSecondPageBinding;

import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class addProductSecondPage extends Fragment {


    private FragmentAddProductSecondPageBinding binding;
    private String  categoryl;
    private String type;

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
        binding=FragmentAddProductSecondPageBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passValues();
            }
        });
        binding.tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new addProduct()).commit();
            }
        });
        setSpinners();

        return view;
    }

    private void setSpinners() {
        //category spinner
        List<String> categorylist= Arrays.asList(getResources().getStringArray(R.array.category));
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,categorylist);
        binding.spCategory.setAdapter(arrayAdapter);
        binding.spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
               categoryl=categorylist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //type spinner

        List<String> typelist= Arrays.asList(getResources().getStringArray(R.array.Type));
        ArrayAdapter<String> arraytypeAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,typelist);
        binding.spType.setAdapter(arraytypeAdapter);
        binding.spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                type=typelist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    private void passValues() {
        if (validateBrandtName()&&validateDiscount()&&validateStock()&&validateSku()){
            addProductFinalPage apf = new addProductFinalPage ();
            Bundle args = new Bundle();
            //getting arguments from previous fragment
            String productName=getArguments().getString("product_name");
            String productDesc=getArguments().getString("product_desc");
            String price=getArguments().getString("product_price");
            String brandName=binding.etBrand.getText().toString().trim();
            String discount= binding.etDiscount.getText().toString().trim();
            String stock=binding.etStock.getText().toString().trim();
            String sku=binding.etSku.getText().toString().trim();
            //passing argument to another fragment
            args.putString("product_name", productName);
            args.putString("product_desc",productDesc);
            args.putString("product_price",price);
            args.putString("stock",stock);
            args.putString("brand",brandName);
            args.putString("discount",discount);
            args.putString("category",categoryl);
            args.putString("type",type);
            args.putString("sku",sku);
            args.putString("imagePath",getArguments().getString("imagePath"));
            apf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,apf).commitAllowingStateLoss();


        }
        else
            Toasty.error(getContext(),"Please fill up everything").show();


    }

    private boolean validateBrandtName(){
        if (binding.etBrand.getText().length()==0)
            return false;
        else
            return true;

    }
    private boolean validateDiscount(){
        if (binding.etDiscount.getText().length()==0)
            return false;
        else return true;

    }
    private boolean validateStock(){

        if (binding.etStock.getText().length()==0)
            return false;
        else return true;
    }

    private boolean validateSku(){

        if (binding.etSku.getText().length()==0)
            return false;
        else return true;
    }

}