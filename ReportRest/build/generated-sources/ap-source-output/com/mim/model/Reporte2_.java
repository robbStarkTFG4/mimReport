package com.mim.model;

import com.mim.model.Trabajo;
import com.mim.model.Usuario2;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-07-26T22:27:49")
@StaticMetamodel(Reporte2.class)
public class Reporte2_ { 

    public static volatile ListAttribute<Reporte2, Trabajo> trabajoList;
    public static volatile SingularAttribute<Reporte2, Date> fecha;
    public static volatile SingularAttribute<Reporte2, String> sitio;
    public static volatile SingularAttribute<Reporte2, Integer> idreporte;
    public static volatile SingularAttribute<Reporte2, Integer> estatus;
    public static volatile SingularAttribute<Reporte2, String> trabajo;
    public static volatile SingularAttribute<Reporte2, Usuario2> usuarioIdusuario;

}