package com.example.laci.kitchenassistant.main.Account;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laci.kitchenassistant.BaseClasses.User;
import com.example.laci.kitchenassistant.R;
import com.example.laci.kitchenassistant.Tools.Validations;
import com.example.laci.kitchenassistant.firebase.Account;
import com.example.laci.kitchenassistant.firebase.RetrieveDataListener;
import com.example.laci.kitchenassistant.main.Account.HistoryDialog.HistoryDialog;
import com.example.laci.kitchenassistant.main.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class AccountFragment extends Fragment implements AccountContract.View{
    private Button change, close;
    private TextInputEditText name, weight,height,age,goal_weight;
    private ImageView profilePicture,coverPhoto,male,female,weight_history;
    //Gender 0 = male
    //Gender 1 = female
    private int lose_keep_gain,gender;
    private Context context;
    private static final int PROFILE_PICTURE_GALLERY = 0;
    private static final int PROFILE_PICTURE_CAMERA = 1;
    private static final int PROFILE_PERMISSION_GALLERY = 2;
    private static final int PROFILE_PERMISSION_CAMERA = 3;
    private static final int COVER_PICTURE_GALLERY = 4;
    private static final int COVER_PICTURE_CAMERA = 5;
    private static final int COVER_PERMISSION_GALLERY = 6;
    private static final int COVER_PERMISSION_CAMERA = 7;

    private View globalView;


    // Editable or unedtiabled mode
    private boolean mode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        context = container.getContext();
        globalView = view;
        initViews(view);
        Glide.with(view).load(R.drawable.icon_scale).into(weight_history);
        setMale();
        setListeners(view);
        Account.downloadProfilePicture(profilePicture,context);
        Account.downloadCoverPhoto(coverPhoto,context);
        updateUser();


        return view;
    }

    private void setListeners(View view){
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mode){
                    changeMode();
                }else{
                    unChangeMode();
                    User user = new User();
//                    Log.e("AAAA",name.getText().toString()+ " " + age.getText().toString() + " " + height.getText().toString() + " " + weight.getText().toString());

                    if(Validations.validateName(Objects.requireNonNull(name.getText()).toString()) == 0)
                        user.setName(name.getText().toString());
                    else
                        name.setError("Not a valid name. It didn't change.");


                    if(Validations.validateAge(Objects.requireNonNull(age.getText()).toString()) == 0)
                        user.setAge(Integer.parseInt(age.getText().toString()));
                    else
                        age.setError("Not a valid age. It didn't change.");

                    if(Validations.validateHeight(Objects.requireNonNull(height.getText()).toString()) == 0)
                        user.setHeight(Integer.parseInt(height.getText().toString()));
                    else
                        height.setError("Not a valid height. It didn't change.");

                    if(Validations.validateWeight(Objects.requireNonNull(weight.getText()).toString())==0)
                        user.setWeight(Integer.parseInt(weight.getText().toString()));
                    else
                        weight.setError("Not a valid weight. It didn't change.");

                    if(Validations.validateWeight(Objects.requireNonNull(goal_weight.getText()).toString())==0)
                        user.setGoal_weight(Integer.parseInt(goal_weight.getText().toString()));
                    else
                        goal_weight.setError("Not a valid weight. It didn't change.");


                    user.setGender(gender);

                    Account.setUser(user);
                    updateUser();

                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode){
                    unChangeMode();
                }
            }
        });



        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode)
                    setMale();
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode)
                    setFemale();
            }
        });



        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountFragment.this.context);

                builder.setMessage("Select the source of the picture.");

                builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{
                                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PROFILE_PERMISSION_GALLERY);
                        } else {

                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, PROFILE_PICTURE_GALLERY);
                        }
                    }
                });

                builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{
                                        android.Manifest.permission.CAMERA,
                                        android.Manifest.permission.CAMERA},
                                    PROFILE_PERMISSION_CAMERA);
                        } else {

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PROFILE_PICTURE_CAMERA);
                        }

                    }
                });

                builder.show();
            }
        });

        coverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountFragment.this.context);

                builder.setMessage("Select the source of the picture.");

                builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{
                                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    COVER_PERMISSION_GALLERY);
                        } else {

                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, COVER_PICTURE_GALLERY);
                        }
                    }
                });

                builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{
                                            android.Manifest.permission.CAMERA,
                                            android.Manifest.permission.CAMERA},
                                    COVER_PERMISSION_CAMERA);
                        } else {

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, COVER_PICTURE_CAMERA);
                        }

                    }
                });

                builder.show();
            }
        });

        weight_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryDialog dialog = new HistoryDialog();
                dialog.show(getFragmentManager(),"HistoryDialog");
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PROFILE_PERMISSION_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PROFILE_PICTURE_GALLERY);

                } else {
                    Toast.makeText(globalView.getContext(),"You can't upload picture from gallery if you don't enabled the access of store.",Toast.LENGTH_LONG).show();
                }
                break;
            case PROFILE_PERMISSION_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, PROFILE_PICTURE_CAMERA);
                } else {
                    Toast.makeText(globalView.getContext(),"You can't upload picture from gallery if you don't enabled the access of camera.",Toast.LENGTH_LONG).show();
                }
                break;
            case COVER_PERMISSION_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, COVER_PICTURE_GALLERY);

                } else {
                    Toast.makeText(globalView.getContext(),"You can't upload picture from gallery if you don't enabled the access of store.",Toast.LENGTH_LONG).show();
                }
                break;
            case COVER_PERMISSION_CAMERA:
                // If request is cancelled, the result arrays are empty.
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, COVER_PICTURE_CAMERA);

                } else {
                    Toast.makeText(globalView.getContext(),"You can't upload picture from gallery if you don't enabled the access of store.",Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PROFILE_PICTURE_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    InputStream istream = null;
                    try {
                        istream = getContext().getContentResolver().openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        Account.uploadProfilePicture(getBytes(istream), new RetrieveDataListener<String>() {
                            @Override
                            public void onSuccess(String data) {
                                Toast.makeText(globalView.getContext(),"The upload was succesfully!",Toast.LENGTH_LONG).show();
                                Account.downloadProfilePicture(profilePicture,context);
                            }

                            @Override
                            public void onFailure(String message) {
                                Toast.makeText(globalView.getContext(),"The upload failed: " + message,Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case PROFILE_PICTURE_CAMERA:
                if(resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                    Account.uploadProfilePicture(baos.toByteArray(), new RetrieveDataListener<String>() {
                        @Override
                        public void onSuccess(String data) {
                            Toast.makeText(globalView.getContext(),"The upload was succesfully!",Toast.LENGTH_LONG).show();
                            Account.downloadProfilePicture(profilePicture,context);
                        }

                        @Override
                        public void onFailure(String message) {
                            Toast.makeText(globalView.getContext(),"The upload failed: " + message,Toast.LENGTH_LONG).show();
                        }
                    });

                }
                break;
            case COVER_PICTURE_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    InputStream istream = null;
                    try {
                        istream = getContext().getContentResolver().openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        Account.uploadCoverPhoto(getBytes(istream), new RetrieveDataListener<String>() {
                            @Override
                            public void onSuccess(String data) {
                                Toast.makeText(globalView.getContext(),"The upload was succesfully!",Toast.LENGTH_LONG).show();
                                Account.downloadCoverPhoto(coverPhoto,context);
                            }

                            @Override
                            public void onFailure(String message) {
                                Toast.makeText(globalView.getContext(),"The upload failed: " + message,Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case COVER_PICTURE_CAMERA:
                if(resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                    Account.uploadCoverPhoto(baos.toByteArray(), new RetrieveDataListener<String>() {
                        @Override
                        public void onSuccess(String data) {
                            Toast.makeText(globalView.getContext(),"The upload was succesfully!",Toast.LENGTH_LONG).show();
                            Account.downloadCoverPhoto(coverPhoto,context);
                        }

                        @Override
                        public void onFailure(String message) {
                            Toast.makeText(globalView.getContext(),"The upload failed: " + message,Toast.LENGTH_LONG).show();
                        }
                    });

                }
                break;

        }
    }

    /**
     * @param inputStream from gallery
     * @return bytes array containing the profile picture
     * @throws IOException
     */
    private byte[] getBytes(InputStream inputStream) throws IOException {

        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void updateUser(){
        Account.getUser(new RetrieveDataListener<User>() {
            @Override
            public void onSuccess(User data) {
                ((MainActivity) getActivity()).user = data;
                updateView();

                Log.e("AAAAAAAAA",((MainActivity)getActivity()).user.toString());
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateView(){
        if(((MainActivity) getActivity()).user != null){
            if(!((MainActivity) Objects.requireNonNull(getActivity())).user.getName().equals("")) name.setText(((MainActivity) getActivity()).user.getName());
            if(((MainActivity) getActivity()).user.getAge() != -1 ) age.setText(String.valueOf(((MainActivity) getActivity()).user.getAge()));
            if(((MainActivity) getActivity()).user.getWeightHistories() != null) weight.setText(String.valueOf(((MainActivity) getActivity()).user.getWeightHistories().get(0).getWeight()));
            if(((MainActivity) getActivity()).user.getHeight() != -1) height.setText(String.valueOf(((MainActivity) getActivity()).user.getHeight()));
            if(((MainActivity) getActivity()).user.getGoal_weight() != -1) goal_weight.setText(String.valueOf(((MainActivity) getActivity()).user.getGoal_weight()));

        }
    }

    private void initViews(View view) {
        change = view.findViewById(R.id.fragment_account_change_save_button);
        close = view.findViewById(R.id.fragment_account_close_button);
        name = view.findViewById(R.id.fragment_account_namet_edittext);
        weight = view.findViewById(R.id.fragment_account_weight_editText);
        height = view.findViewById(R.id.fragment_account_height_edittext);
        age = view.findViewById(R.id.fragment_account_age_edittext);
        profilePicture = view.findViewById(R.id.fragment_account_profile_picture);
        coverPhoto = view.findViewById(R.id.fragment_account_cover_photo);
        male = view.findViewById(R.id.fragment_account_gender_male);
        female = view.findViewById(R.id.fragment_account_gender_female);
        weight_history = view.findViewById(R.id.fragment_account_weight_history);
        goal_weight = view.findViewById(R.id.fragment_account_goal_weight_editText);

        unChangeMode();
        lose_keep_gain = 0;

    }

    //We can edit the fields.
    private void changeMode(){
        name.setEnabled(true);
        age.setEnabled(true);
        weight.setEnabled(true);
        height.setEnabled(true);
        close.setVisibility(View.VISIBLE);
        change.setText("SAVE");
        mode = true;
    }

    //We can't change the content of fields.
    private void unChangeMode(){
        name.setEnabled(false);
        age.setEnabled(false);
        weight.setEnabled(false);
        height.setEnabled(false);
        close.setVisibility(View.INVISIBLE);
        change.setText("CHANGE");
        mode = false;
    }



    private void setMale(){
        gender = 0;
        male.setImageTintList(ColorStateList.valueOf(getActivity().getResources().getColor(R.color.colorBlack)));
        female.setImageTintList(ColorStateList.valueOf(getActivity().getResources().getColor(R.color.colorGray)));
    }

    private void setFemale(){
        gender = 1;
        female.setImageTintList(ColorStateList.valueOf(getActivity().getResources().getColor(R.color.colorBlack)));
        male.setImageTintList(ColorStateList.valueOf(getActivity().getResources().getColor(R.color.colorGray)));
    }

}
