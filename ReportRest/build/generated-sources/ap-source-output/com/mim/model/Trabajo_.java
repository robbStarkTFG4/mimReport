package com.mim.model;

import com.mim.model.Actividades;
import com.mim.model.Reporte2;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-07-26T22:27:49")
@StaticMetamodel(Trabajo.class)
public class Trabajo_ { 

    public static volatile SingularAttribute<Trabajo, String> descripcion;
    public static volatile ListAttribute<Trabajo, Actividades> actividadesList;
    public static volatile SingularAttribute<Trabajo, String> imagen;
    public static volatile SingularAttribute<Trabajo, Integer> idtrabajo;
    public static volatile SingularAttribute<Trabajo, Reporte2> reporteIdreporte;

}