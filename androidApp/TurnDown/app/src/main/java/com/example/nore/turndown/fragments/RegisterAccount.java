package com.example.nore.turndown.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.nore.turndown.MainActivity;
import com.example.nore.turndown.R;
import com.example.nore.turndown.backEnd.models.Usuario2;
import com.example.nore.turndown.backEnd.services.ReportService;
import com.example.nore.turndown.entity.dao.DaoSession;
import com.example.nore.turndown.entity.dao.Usuario;
import com.example.nore.turndown.entity.dao.UsuarioDao;
import com.example.nore.turndown.util.tags.FragmentTags;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class RegisterAccount extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // FORM OBJECTS
    private EditText usuarioField;
    private EditText nombreField;
    private EditText apellidoPaternoField;
    private EditText maternoField;
    private Button sendInfoBtn;
    //END FORM OBJECTS

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterAccount.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterAccount newInstance(String param1, String param2) {
        RegisterAccount fragment = new RegisterAccount();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RegisterAccount() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_account, container, false);

        usuarioField = (EditText) view.findViewById(R.id.userField);
        nombreField = (EditText) view.findViewById(R.id.nombreField);
        apellidoPaternoField = (EditText) view.findViewById(R.id.paternoField);
        maternoField = (EditText) view.findViewById(R.id.maternoField);

        sendInfoBtn = (Button) view.findViewById(R.id.saveDataForm);
        sendInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usuario = usuarioField.getText().toString();
                final String nombre = nombreField.getText().toString();
                final String apellidoPaterno = apellidoPaternoField.getText().toString();
                final String materno = maternoField.getText().toString();

                if ((usuario.length() > 0) && (nombre.length() > 0) && (apellidoPaterno.length() > 0) && (materno.length() > 0)) {
                    //persist user
                    //Toast.makeText(getActivity(), "Guardando.....", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar
                            .make(((MainActivity) getActivity()).getParentLayout(), "Registrando usuario.....", Snackbar.LENGTH_INDEFINITE)
                            .setActionTextColor(getResources().getColor(R.color.secondary_texto));
                    ViewGroup group = (ViewGroup) snackbar.getView();
                    group.setBackgroundColor(getResources().getColor(R.color.alerta));

                    snackbar.show();

                    try {

                        RestAdapter restAdapter = new RestAdapter.Builder()
                                .setEndpoint(ReportService.BASE_URL)
                                .build();

                        ReportService apiService =
                                restAdapter.create(ReportService.class);


                        Usuario2 us = new Usuario2();
                        us.setApellidos(apellidoPaterno + " " + materno);
                        us.setNombre(nombre);
                        us.setUsuario(usuario);

                        apiService.verifyUser(us, new Callback<Usuario2>() {
                            @Override
                            public void success(Usuario2 usuario2, Response response) {
                                if (response != null) {
                                    int status = response.getStatus();
                                    if (status == 200) {
                                        persistUser(usuario, nombre, apellidoPaterno, materno);
                                    } else if (status == 409) {
                                        Snackbar
                                                .make(((MainActivity) getActivity()).getParentLayout(), "Usuario no disponible", Snackbar.LENGTH_LONG)
                                                .show();
                                        //Toast.makeText(getActivity(), "Usuario no disponible", Toast.LENGTH_LONG).show();
                                    } else {
                                        Snackbar
                                                .make(((MainActivity) getActivity()).getParentLayout(), "Hubo algun error", Snackbar.LENGTH_LONG)
                                                .show();
                                        //Toast.makeText(getActivity(), "Hubo algun error", Toast.LENGTH_LONG).show();
                                    }
                                }
                                //end
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                if (error.getLocalizedMessage().equals("409 Conflict")) {
                                    Snackbar
                                            .make(((MainActivity) getActivity()).getParentLayout(), "Usuario no disponible", Snackbar.LENGTH_LONG)
                                            .show();
                                    //Toast.makeText(getActivity(), "Usuario no disponible", Toast.LENGTH_LONG).show();
                                } else {
                                    Snackbar
                                            .make(((MainActivity) getActivity()).getParentLayout(), error.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                                            .show();
                                    //Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                    } catch (Exception e) {
                        Snackbar
                                .make(((MainActivity) getActivity()).getParentLayout(), "Hubo algun error", Snackbar.LENGTH_LONG)
                                .show();
                        //Toast.makeText(getActivity(), "Hubo algun error", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar
                            .make(((MainActivity) getActivity()).getParentLayout(), "llena todos los campos", Snackbar.LENGTH_LONG)
                            .show();
                    //Toast.makeText(getActivity(), "llena todos los campos", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    private void persistUser(String usuario, String nombre, String apellidoPaterno, String materno) {
        DaoSession session = ((MainActivity) getActivity()).session;
        UsuarioDao userDao = session.getUsuarioDao();

        Usuario user = new Usuario();
        user.setUsuario(usuario);
        user.setNombre(nombre);
        user.setApellido(apellidoPaterno + " " + materno);
        user.setContraseña("1234");
        user.setActiveUser(true);
        userDao.insert(user);

        Snackbar
                .make(((MainActivity) getActivity()).getParentLayout(), "USUARIO CREADO", Snackbar.LENGTH_LONG)
                .show();
        //Toast.makeText(getActivity(), "USUARIO CREADO", Toast.LENGTH_LONG).show();

        cleanFields();

        AddJobFragment obj = new AddJobFragment();
        obj.setTrans((MainActivity) getActivity());

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, obj, FragmentTags.ADD_JOB)
                .commit();
    }

    public void cleanFields() {
        usuarioField.setText("");
        nombreField.setText("");
        apellidoPaternoField.setText("");
        maternoField.setText("");
    }

}
