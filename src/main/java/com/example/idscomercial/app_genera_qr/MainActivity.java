package com.example.idscomercial.app_genera_qr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String charset = "UTF-8";

    public EditText et_concepto, et_cantidad, et_nombre;
    public Spinner sp_incertidumbre, sp_cuentaClabe;
    public ImageView iv_qr;
    public Switch sw_imagen;
    public Button btn_generarQR;

    public ArrayAdapter adapter, adapter1;

    public String strItemIncertidumbre, strCuentaClabe, strTdc, json;
    public Boolean isTdc = false, isLogo = false;
    public CreatQRFromJson qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_concepto = findViewById(R.id.ed_concepto);
        et_cantidad = findViewById(R.id.ed_cantidad);
        et_nombre = findViewById(R.id.ed_nombre);

        iv_qr = findViewById(R.id.iv_qr);

        sp_incertidumbre = findViewById(R.id.sp_incertidumbre);
        sp_cuentaClabe = findViewById(R.id.sp_cuentaClable);

        sw_imagen = findViewById(R.id.sw_imagen);
        btn_generarQR = findViewById(R.id.bt_generar);

        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item,
                SimulationData.incertidumbre);

        sp_incertidumbre.setAdapter(adapter);

        adapter1 = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item,
                SimulationData.clabeTdc);

        sp_cuentaClabe.setAdapter(adapter1);

        setListeners();
    }

    public void setListeners() {
        sp_incertidumbre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strItemIncertidumbre = adapterView.getItemAtPosition(i).toString();

                Log.d(LOG_TAG + ": ", "seleccion de % de incertidumbre:: " + strItemIncertidumbre);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        sp_cuentaClabe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String localData = adapterView.getItemAtPosition(i).toString();

                Log.d(LOG_TAG + ": ", "seleccion de cuenta o TDC:: " + localData);

                switch (localData) {
                    case "1500003862":
                        setValues("", false, SimulationData.dataClabeQr[0]);
                        break;
                    case "1205791510":
                        setValues("", false, SimulationData.dataClabeQr[1]);
                        break;
                    case "1500000077":
                        setValues("", false, SimulationData.dataClabeQr[2]);
                        break;
                    case "0102196018":
                        setValues("", false, SimulationData.dataClabeQr[3]);
                        break;
                    case "4152313304428563":
                        setValues(SimulationData.dataTdcQr[0], true, "");
                        break;
                    case "4555041870000184":
                        setValues(SimulationData.dataTdcQr[1], true, "");
                        break;
                    case "4555041000002290":
                        setValues(SimulationData.dataTdcQr[2], true, "");
                        break;
                    case "4555042602360516":
                        setValues(SimulationData.dataTdcQr[3], true, "");
                        break;
                    case "4555042700367868":
                        setValues(SimulationData.dataTdcQr[4], true, "");
                        break;
                    case "4152313304428837":
                        setValues(SimulationData.dataTdcQr[5], true, "");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        sw_imagen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isLogo = b;

                Log.d(LOG_TAG + ": ", "bandera relacionada con el logo " + isLogo);
            }
        });

        btn_generarQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qr = new CreatQRFromJson();
                json = qr.crearJson(et_cantidad.getText().toString(), et_concepto.getText().toString(),
                        et_nombre.getText().toString(), isTdc, strTdc, strCuentaClabe);

                Log.d(LOG_TAG + ": ", "JSON Respuesta --> " + json);

                iv_qr.setImageBitmap(qr.createQR(json, charset, iv_qr.getLayoutParams().width,
                        strItemIncertidumbre, MainActivity.this, isLogo));

                Log.d(LOG_TAG + ": ", "se ha generado el QR correctamente");
            }
        });
    }

    public void setValues(String numTdc, Boolean isTDC, String numCuentaClabe) {
        strTdc = numTdc;
        isTdc = isTDC;
        strCuentaClabe = numCuentaClabe;

        Log.d(LOG_TAG + ": ", "valores guardados para formar el JSON:: " +
                " numero TDC: " + numTdc +
                " numero CLABE: " + numCuentaClabe +
                " bandera TDC: " + isTDC);
    }
}