package com.example.smartseller.ui.home.MyProductFragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.smartseller.R;
import com.example.smartseller.data.model.ColorAttribute;
import com.example.smartseller.data.model.Products;
import com.example.smartseller.data.model.SizeAttribute;
import com.example.smartseller.data.network.JsonResponse;
import com.example.smartseller.data.network.SmartAPI;
import com.example.smartseller.databinding.FragmentMyProductDetailsEditBinding;
import com.example.smartseller.ui.home.HomeActivity;
import com.example.smartseller.util.session.Session;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;


public class MyProductDetailsEdit extends Fragment {
    private FragmentMyProductDetailsEditBinding binding;
    private final int GALLERY_REQUEST=10002;
    private String imgPath="";
    private String picturePath="";
    private Session session;
    Integer productId;
    private String categoryl;
    private String type;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentMyProductDetailsEditBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        session=new Session(getContext());
        getPassedValues();
        setSpinners();
        binding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAndPutValues();
            }
        });
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyProductDetails mpd=new MyProductDetails();
                Bundle args=new Bundle();
                args.putParcelable("productObj",getArguments().getParcelable("productObj"));
                mpd.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mpd).commit();

                      }
        });
        return view;
    }

    private void getAndPutValues() {
        String picture_path="";
        if (imgPath.length()>2)
        {
            File f = new File(imgPath);
            picture_path = f.getName();
        uploadImage();
            String base_url = "127.0.0.1/smartPasalAssets/photos/"; //for storing in db
            picture_path= base_url +picture_path;
        }
        else
            picture_path=picturePath;
        String product_name=binding.etProductName.getText().toString().trim();
        String description=binding.etDesc.getText().toString().trim();
        String discountStr=binding.etDiscount.getText().toString().trim();
        Integer discount=Integer.valueOf(discountStr);
        String category=categoryl;
        String brand=binding.etBrand.getText().toString().trim();

        String price=binding.etPrice.getText().toString().trim();
        String stockstr=binding.etStock.getText().toString().trim();
        Integer stock=Integer.valueOf(stockstr);
        String sku=binding.etSku.getText().toString().trim();
        Integer sellerId=session.getUserId();

        Products products=new Products(productId,product_name, description, price, category, brand,  sku, type, picture_path, discount,  stock,sellerId);
        Call<JsonResponse> updateProduct= SmartAPI.getApiService().updateProduct(session.getJWT(),products);
        updateProduct.enqueue(new retrofit2.Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getStatus().equalsIgnoreCase("200 OK"))
                    {
                        Toasty.success(getContext(),response.body().getMessage()).show();


                        addAttributes(productId);
                    }
                    else
                        Toasty.error(getContext(),response.body().getMessage()).show();
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        requestPermission();
    }

    private void getPassedValues() {
        Bundle b=getArguments();
        Products products=b.getParcelable("productObj");
        Toasty.success(getContext(),products.getProductName()).show();
        binding.etProductName.setText(products.getProductName());
        binding.etDesc.setText(products.getDesc());
        binding.etPrice.setText(String.valueOf(products.getPrice()));
        binding.etBrand.setText(products.getBrand());

        binding.etSku.setText(products.getSku());
        binding.etDiscount.setText(String.valueOf(products.getDiscount()));
        binding.etStock.setText(String.valueOf(products.getStock()));



        productId=products.getProductId();
        picturePath=products.getPicture_path();
        binding.etColor.setText(products.getColor());
        binding.etSize.setText(products.getSize());

        try{
            String url=products.getPicture_path();
            Picasso.get()
                    .load(url)
                    .fit()
                    .into(binding.ivProductImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Load",e.getMessage());
                        }
                    });}
        catch (Exception e){
            Log.d("error",e.getMessage());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        binding.ivProductImage.setImageBitmap(imageBitmap);
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
                        Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        imgPath=picturePath;





                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
    }

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat
                    .requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        }
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

    private void addAttributes(Integer productId) {
        String color=binding.etColor.getText().toString().trim();
        String sizeStr=binding.etSize.getText().toString().trim();
        if (sizeStr.isEmpty()&&color.isEmpty())
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MyProduct()).commit();
                ColorAttribute colorAttribute=new ColorAttribute(productId,color);
                Call<JsonResponse> addColor=SmartAPI.getApiService().addColor(session.getJWT(),colorAttribute);
                addColor.enqueue(new retrofit2.Callback<JsonResponse>() {
                    @Override
                    public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                        if (response.isSuccessful())
                        {

                        }

                    }

                    @Override
                    public void onFailure(Call<JsonResponse> call, Throwable t) {

                    }
                });
                SizeAttribute sizeAttribute=new SizeAttribute(productId,sizeStr);
                Call<JsonResponse> sizeCall=SmartAPI.getApiService().addSize(session.getJWT(),sizeAttribute);
                sizeCall.enqueue(new retrofit2.Callback<JsonResponse>() {
                    @Override
                    public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                        if (response.isSuccessful())
                            Log.d("sizeCall", "onResponse:"+response.body().getMessage());

                    }

                    @Override
                    public void onFailure(Call<JsonResponse> call, Throwable t) {

                    }
                });

            }





    private void setSpinners() {
        //category spinner
        List<String> categorylist= Arrays.asList(getResources().getStringArray(R.array.category));
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,categorylist);
        binding.spCategory.setAdapter(arrayAdapter);
        int positionOfSelectedCategory = 0;
        Products products = getArguments().getParcelable("productObj");
        String selectedCategory = products.getCategory();
        for (String category:categorylist){
            if (category.equalsIgnoreCase(selectedCategory))
                break;
            positionOfSelectedCategory++;
        }


        binding.spCategory.setSelection(positionOfSelectedCategory,true);


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
        int positionOfSelectedType = 0;
        String selectedType = products.getType();
        for (String type:typelist){
            if (type.equalsIgnoreCase(selectedType))
                break;
            positionOfSelectedType++;
        }

        binding.spType.setSelection(positionOfSelectedType,true);

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

    private void uploadImage(){

        File file = new File(imgPath);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);


        Call<JsonResponse> uploadCall=SmartAPI.getApiService().uploadImage(session.getJWT(),body);
        uploadCall.enqueue(new retrofit2.Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getStatus().equalsIgnoreCase("200 OK"))
                        Log.d("edit image upload", "onResponse: image upload successfull");
                }




            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                Toasty.error(getContext(),t.getMessage()).show();

            }
        });



    }


}