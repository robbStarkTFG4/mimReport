package com.example.nore.turndown.util.interfaces;

import com.example.nore.turndown.entity.dao.Reporte;

/**
 * Created by NORE on 11/07/2015.
 */
public interface SaveListener {
    public void saveResult(Boolean res,Long id,Reporte repo);
}
