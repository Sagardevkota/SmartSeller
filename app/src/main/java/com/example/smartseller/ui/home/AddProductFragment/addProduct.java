package com.example.smartseller.ui.home.AddProductFragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.smartseller.R;
import com.example.smartseller.databinding.FragmentAddProductBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import es.dmoral.toasty.Toasty;


public class addProduct extends Fragment {

    private FragmentAddProductBinding binding;
    private final int GALLERY_REQUEST=10002;
    private String imgPath="";




    public addProduct() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddProductBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passValue();
            }
        });
        binding.btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
        return view;
    }

    private void passValue(){
        addProductSecondPage apf = new addProductSecondPage ();
        Bundle args = new Bundle();
        if (validateProductName()&&validateProductDesc()&&validatePrice()&&validateImage())
        {
            String product_name=binding.etProductName.getText().toString().trim();
            String product_desc=binding.etProductDesc.getText().toString().trim();
            String price=binding.etPrice.getText().toString().trim();
            args.putString("product_name", product_name);
            args.putString("product_desc",product_desc);
            args.putString("product_price",price);
            args.putString("imagePath",imgPath);
            apf.setArguments(args);

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, apf).commit();

        }
        else
            Toasty.error(getContext(),"Fill up valid information").show();
          }

          private boolean validateProductName(){
             if (binding.etProductName.getText().length()==0)
                 return false;
                 else
                     return true;

          }
          private boolean validateProductDesc(){
           if (binding.etProductDesc.getText().length()==0)
               return false;
           else return true;

          }
          private boolean validatePrice(){

              if (binding.etPrice.getText().length()==0)
                  return false;
              else return true;
          }
          private boolean validateImage(){
        if (imgPath.length()<2)
            return false;
        else return true;
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
                        binding.productImage.setImageBitmap(imageBitmap);
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


}