package com.example.administradorpanoramaregional;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class AGREGAR extends AppCompatActivity implements View.OnClickListener{
    EditText titulo, subtitulo, nota, imagensub, fecha;
    Spinner seccion,lugar,importancia;
    Button agregar, enviar;

    ImageView imageToUpload;
    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;
    String UPLOAD_URL = "http://panoramaregionalvds.com/upload.php";

    String KEY_IMAGE = "foto";
    String KEY_NOMBRE = "nombre";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregarnota);

            titulo=(EditText) findViewById(R.id.editText);
            subtitulo=(EditText)findViewById(R.id.editText2);
            nota = (EditText)findViewById(R.id.editText14);
            fecha = (EditText)findViewById(R.id.editText13);
            seccion = (Spinner)findViewById(R.id.spinner);
            lugar = (Spinner)findViewById(R.id.spinner2);
            importancia = (Spinner)findViewById(R.id.spinner3);

            imagensub=(EditText)findViewById(R.id.editText3);
            imageToUpload=(ImageView)findViewById(R.id.imagendos);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.secciones,android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            seccion.setAdapter(adapter);
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.ubicacion, android.R.layout.simple_spinner_item);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lugar.setAdapter(adapter1);
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource( this, R.array.importancia,android.R.layout.simple_spinner_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            importancia.setAdapter(adapter2);

            enviar = (Button) findViewById(R.id.button7);
            agregar = (Button) findViewById(R.id.button5);

            agregar.setOnClickListener(this);
            enviar.setOnClickListener(this);
        }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button7:
                showFileChooser();
                break;
            case R.id.button5:
                if(titulo.getText().toString().equals("")){
                    Toast.makeText(this, "Te falta ingresar el titulo", Toast.LENGTH_SHORT).show();
                }else {
                    if (subtitulo.getText().toString().equals("")){
                        Toast.makeText(this, "Te falta ingresar el subtitulo", Toast.LENGTH_SHORT).show();
                    }else{
                        if (fecha.getText().toString().equals("")){
                            Toast.makeText(this, "Te falta ingresar el fecha", Toast.LENGTH_SHORT).show();
                        }else{
                            if (nota.getText().toString().equals("")){
                                Toast.makeText(this, "Te falta ingresar el nota", Toast.LENGTH_SHORT).show();
                            }else{
                                uploadImage();
                                insertar();
                                finish();
                            }

                        }
                    }
                }

                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 350 /*Ancho*/, 250 /*Alto*/, false /* filter*/);
                //Configuración del mapa de bits en ImageView
                imageToUpload.setImageBitmap(resizedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleciona imagen"), PICK_IMAGE_REQUEST);
    }

    private void insertar(){
        final String Atitulo = titulo.getText().toString().trim();
        final String Asubtitutlo = subtitulo.getText().toString().trim();
        final String Anota = nota.getText().toString().trim();
        final String Afecha = fecha.getText().toString().trim();
        final String Aseccion = seccion.getSelectedItem().toString().trim();
        final String Alugar = lugar.getSelectedItem().toString().trim();
        final String Aimportancia = importancia.getSelectedItem().toString().trim();
        final String Aimagen = imagensub.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, "http://panoramaregionalvds.com/alta_nota.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Datos insertados")) {

                    Toast.makeText(AGREGAR.this, "Datos ingresados", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AGREGAR.this,MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AGREGAR.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AGREGAR.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<String, String>();

                params.put("titulo", Atitulo);
                params.put("subtitulo", Asubtitutlo);
                params.put("nota", Anota);
                params.put("fecha", Afecha);
                params.put("seccion", Aseccion);
                params.put("lugar", Alugar);
                params.put("importancia", Aimportancia);
                params.put("imagen", Aimagen + ".jpg");

                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AGREGAR.this);
        requestQueue.add(request);

    }

    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadImage() {
        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(AGREGAR.this, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(AGREGAR.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imagen = getStringImagen(bitmap);
                String nombre = imagensub.getText().toString().trim();

                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_IMAGE, imagen);
                params.put(KEY_NOMBRE, nombre);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

