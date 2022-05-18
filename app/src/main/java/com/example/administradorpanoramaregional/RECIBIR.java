package com.example.administradorpanoramaregional;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.snowdream.android.widget.SmartImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class RECIBIR extends AppCompatActivity implements View.OnClickListener{
    EditText titulo,subtitulo,nota,fecha,nombreimagen;
    SmartImageView smartImageView;
    TextView idnota,seccion,lugar,importancia,imagenuno;
    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView imageToUploaddos;
    Button traerimagen,actualizar,eliminar;
    ProgressDialog pDialog;

    JsonObjectRequest jsonObjectRequest;

    ImageView imageToUpload;
    Bitmap bitmap,bitmapdos;
    int PICK_IMAGE_REQUEST = 1;
    String UPLOAD_URL = "http://panoramaregionalvds.com/uploaddos.php";

    String KEY_IMAGE = "foto";
    String KEY_NOMBRE = "nombre";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recibir);

        idnota = (TextView)findViewById(R.id.textView22);
        titulo = (EditText)findViewById(R.id.editText15);
        subtitulo = (EditText)findViewById(R.id.editText16);
        nota = (EditText)findViewById(R.id.editText18);
        fecha = (EditText)findViewById(R.id.editText19);
        imagenuno = (TextView)findViewById(R.id.textView26);
        seccion = (TextView)findViewById(R.id.textView23);
        lugar = (TextView)findViewById(R.id.textView24);
        importancia = (TextView)findViewById(R.id.textView25);
        nombreimagen = (EditText)findViewById(R.id.edt_nombre_imagen);
        traerimagen = (Button)findViewById(R.id.button10);

        imageToUploaddos=(ImageView)findViewById(R.id.imagencuatro);
        actualizar=(Button)findViewById(R.id.button12);
        eliminar=(Button)findViewById(R.id.button13);

        final Bundle parametros = getIntent().getExtras();
        if(parametros != null){
            idnota.setText(parametros.getString("a"));
            titulo.setText(parametros.getString("b"));
            subtitulo.setText(parametros.getString("c"));
            nota.setText(parametros.getString("d"));
            fecha.setText(parametros.getString("e"));
            imagenuno.setText(parametros.getString("i"));
            seccion.setText(parametros.getString("f"));
            lugar.setText(parametros.getString("g"));
            importancia.setText(parametros.getString("h"));
        }else{

        }

        smartImageView = (SmartImageView) findViewById(R.id.imagentres);
        final String urlfinal ="http://panoramaregionalvds.com/imagenes/"+imagenuno.getText().toString();
        Rect rect = new Rect(smartImageView.getLeft(),smartImageView.getTop(),smartImageView.getRight(),smartImageView.getBottom());
        smartImageView.setImageUrl(urlfinal, rect);

        traerimagen.setOnClickListener(this);
        actualizar.setOnClickListener(this);
        eliminar.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageToUploaddos.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button10:
                Intent galleryIntentdos = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntentdos, RESULT_LOAD_IMAGE);
                break;
            case R.id.button12:
                if(imageToUploaddos.getDrawable() == null){
                    solo();
                }else{
                    nosolo();
                }
                break;
            case R.id.button13:
                eliminardos();
                break;
        }
    }


    public void solo(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(RECIBIR.this);

        builder.setMessage("¿No ha cambiado de imagen, ¿desea seguir?")
                .setTitle("Confirmacion")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int id) {
                        webServiceActualizar();
                        Log.i("Dialogos", "Confirmacion Aceptada.");
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Cancelada.");
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    public void nosolo(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(RECIBIR.this);

        builder.setMessage("Ha cambiado de imagen, ¿desea seguir?")
                .setTitle("Confirmacion")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int id) {
                        webServiceActualizar();
                        uploadImage();
                        Log.i("Dialogos", "Confirmacion Aceptada.");
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Cancelada.");
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    public void eliminardos(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(RECIBIR.this);

        builder.setMessage("Segura que desea eliminar esta noticia, ¿desea seguir?")
                .setTitle("Confirmacion")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int id) {
                        webServiceEliminar();
                        Log.i("Dialogos", "Confirmacion Aceptada.");
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Cancelada.");
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    //ACTUALIZAR

    private void webServiceActualizar() {
        final String idnotauno = idnota.getText().toString().trim();
        final String titulouno = titulo.getText().toString().trim();
        final String subtitulouno = subtitulo.getText().toString().trim();
        final String notauno = nota.getText().toString().trim();
        final String fechauno = fecha.getText().toString().trim();
        final String nombreimg = imagenuno.getText().toString().trim();
        final String imgnombr = nombreimagen.getText().toString().trim();

        pDialog=new ProgressDialog(RECIBIR.this);
        pDialog.setMessage("Actualizando...");
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://panoramaregionalvds.com/updatemovil.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Datos actualizados")) {

                    Toast.makeText(RECIBIR.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RECIBIR.this,MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RECIBIR.this, response, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RECIBIR.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RECIBIR.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                // pDialog.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<>();
                parametros.put("idnota",idnotauno);
                parametros.put("titulo",titulouno);
                parametros.put("subtitulo",subtitulouno);
                parametros.put("nota",notauno);
                parametros.put("fecha",fechauno);
                parametros.put("imagen", imgnombr + ".jpg");
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RECIBIR.this);
        requestQueue.add(stringRequest);
    }

    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadImage() {
        //  final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //     loading.dismiss();
                        Toast.makeText(RECIBIR.this, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loading.dismiss();
                // Toast.makeText(RECIBIR.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imagen = getStringImagen(bitmap);
                String nombre = nombreimagen.getText().toString().trim();
                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_IMAGE, imagen);
                params.put(KEY_NOMBRE, nombre);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //ELIMINAR
    private void webServiceEliminar() {
        final String idnotauno = idnota.getText().toString().trim();
        pDialog=new ProgressDialog(RECIBIR.this);
        pDialog.setMessage("Eliminando...");
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://panoramaregionalvds.com/deletemovil.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();
                if (response.equalsIgnoreCase("elimina")){
                    Toast.makeText(RECIBIR.this,"Se ha Eliminado con exito",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RECIBIR.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    if (response.equalsIgnoreCase("noExiste")){
                        Toast.makeText(RECIBIR.this,"No se encuentra la persona para eliminar",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(RECIBIR.this,"No se ha Eliminado ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RECIBIR.this,"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<>();
                parametros.put("documento",idnotauno);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RECIBIR.this);
        requestQueue.add(stringRequest);
    }
}
