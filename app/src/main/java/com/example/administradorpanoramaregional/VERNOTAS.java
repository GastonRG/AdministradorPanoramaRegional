package com.example.administradorpanoramaregional;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class VERNOTAS extends AppCompatActivity {
    ArrayList valor1= new ArrayList();
    ArrayList valor2= new ArrayList();
    ArrayList valor3= new ArrayList();
    ArrayList valor4= new ArrayList();
    ArrayList valor5= new ArrayList();
    ArrayList valor6= new ArrayList();
    ArrayList valor7= new ArrayList();
    ArrayList valor8= new ArrayList();
    ArrayList valor9= new ArrayList();
    ListView lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vernotas);
        lista = (ListView)findViewById(R.id.lista);
        descargarimagen12();
    }

    private void descargarimagen12(){
        valor1.clear();
        valor2.clear();
        valor3.clear();
        valor4.clear();
        valor5.clear();
        valor6.clear();
        valor7.clear();
        valor8.clear();
        valor9.clear();
        final ProgressDialog progressDialog=new ProgressDialog(VERNOTAS.this);
        progressDialog.setMessage("Cargando Notas...");
        progressDialog.show();
        AsyncHttpClient client= new AsyncHttpClient();
        client.get("http://panoramaregionalvds.com/vernota.php", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    progressDialog.dismiss();
                    try {
                        final JSONArray jsonArray = new JSONArray(new String(responseBody));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            valor1.add(jsonArray.getJSONObject(i).getString("idnota"));
                            valor2.add(jsonArray.getJSONObject(i).getString("titulo"));
                            valor3.add(jsonArray.getJSONObject(i).getString("subtitulo"));
                            valor4.add(jsonArray.getJSONObject(i).getString("nota"));
                            valor5.add(jsonArray.getJSONObject(i).getString("fecha"));
                            valor6.add(jsonArray.getJSONObject(i).getString("seccion"));
                            valor7.add(jsonArray.getJSONObject(i).getString("lugar"));
                            valor8.add(jsonArray.getJSONObject(i).getString("importancia"));
                            valor9.add(jsonArray.getJSONObject(i).getString("imagen"));
                        }
                        lista.setAdapter(new ImagenAdapter12(getApplicationContext()));
                        if(lista.getCount()!=0){
                            lista.setAdapter(new ImagenAdapter12(getApplicationContext()));
                        }else{
                            final ProgressDialog progressDialog1=new ProgressDialog(VERNOTAS.this);
                            progressDialog1.setMessage("No se encuentra Información...");
                            progressDialog1.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    private class ImagenAdapter12 extends BaseAdapter {
        Context ctx;
        LayoutInflater layoutInflater;
        TextView tvtitulo, tvdescripcion, opcional;
        SmartImageView smartImageView;
        public ImagenAdapter12(Context applicationContext) {
            this.ctx=applicationContext;
            layoutInflater=(LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return valor1.size();
        }
        @Override
        public Object getItem(int position) {
            return position;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.activity_main_item, null);
            tvtitulo = (TextView) viewGroup.findViewById(R.id.tvtitulo);
            tvdescripcion = (TextView) viewGroup.findViewById(R.id.tvdescripcion);
            opcional=(TextView) viewGroup.findViewById(R.id.opcional);

            tvtitulo.setText(valor2.get(position).toString());
            tvdescripcion.setText(valor3.get(position).toString());
            opcional.setText(valor5.get(position).toString());
            smartImageView = (SmartImageView) viewGroup.findViewById(R.id.imagen1);
            final String urlfinal ="http://panoramaregionalvds.com/imagenes/"+valor9.get(position).toString();
            Rect rect = new Rect(smartImageView.getLeft(),smartImageView.getTop(),smartImageView.getRight(),smartImageView.getBottom());
            smartImageView.setImageUrl(urlfinal, rect);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent nuevoform1 = new Intent(VERNOTAS.this,RECIBIR.class);
                    nuevoform1.putExtra("a", valor1.get(position).toString());
                    nuevoform1.putExtra("b", valor2.get(position).toString());
                    nuevoform1.putExtra("c", valor3.get(position).toString());
                    nuevoform1.putExtra("d", valor4.get(position).toString());
                    nuevoform1.putExtra("e", valor5.get(position).toString());
                    nuevoform1.putExtra("f", valor6.get(position).toString());
                    nuevoform1.putExtra("g", valor7.get(position).toString());
                    nuevoform1.putExtra("h", valor8.get(position).toString());
                    nuevoform1.putExtra("i", valor9.get(position).toString());
                    startActivity(nuevoform1);
                    finish();
                }
            });
            return viewGroup;
        }
    }
}
