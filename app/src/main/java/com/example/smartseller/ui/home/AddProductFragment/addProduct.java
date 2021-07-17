package com.example.smartseller.ui.home.AddProductFragment;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.smartseller.R;
import com.example.smartseller.data.model.Products;
import com.example.smartseller.databinding.FragmentAddProductBinding;
import com.example.smartseller.ui.home.HomeActivity;
import com.example.smartseller.ui.home.homeFragment.home;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import es.dmoral.toasty.Toasty;


public class addProduct extends Fragment {

    private FragmentAddProductBinding binding;
    private final int GALLERY_REQUEST = 10002;
    private String imgPath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        ((HomeActivity)requireActivity()).fabMsgVisibility(false);
        clearField();
        binding.tvNext.setOnClickListener(view1 -> passValue(view1));
        binding.btnSelectImage.setOnClickListener(view12 -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        });

        return view;
    }

    private void clearField() {
        binding.etProductName.getText().clear();
        binding.etProductName.getText().clear();
        binding.etPrice.getText().clear();

    }

    private void passValue(View view) {

        if (validateProductName() && validateProductDesc() && validatePrice() && validateImage()) {
            String product_name = binding.etProductName.getText().toString().trim();
            String product_desc = binding.etProductDesc.getText().toString().trim();
            String price = binding.etPrice.getText().toString().trim();
            //only setting name desc and price in first fragment.. others are null
            Products products = Products.builder()
                    .productName(product_name)
                    .desc(product_desc)
                    .price(price)
                    .picturePath(imgPath)
                    .build();

            addProductDirections.ActionProductSecondPage actionProductSecondPage =
                    addProductDirections.actionProductSecondPage(products);
            Navigation.findNavController(view).navigate(actionProductSecondPage);

        } else
            Toasty.error(getContext(), "Fill up valid information").show();
    }

    private boolean validateProductName() {
        return binding.etProductName.getText().length() != 0;

    }

    private boolean validateProductDesc() {
        return binding.etProductDesc.getText().length() != 0;

    }

    private boolean validatePrice() {
        return binding.etPrice.getText().length() != 0;
    }

    private boolean validateImage() {
        return imgPath.length() >= 2;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            if (requestCode == GALLERY_REQUEST) {
                Uri selectedImage = data.getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    binding.productImage.setImageBitmap(imageBitmap);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    imgPath = picturePath;


                } catch (IOException e) {
                    Log.i("TAG", "Some exception " + e);
                }
            }
    }

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // Permission Denied
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}