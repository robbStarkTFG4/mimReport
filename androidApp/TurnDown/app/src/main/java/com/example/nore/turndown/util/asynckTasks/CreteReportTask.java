package com.example.nore.turndown.util.asynckTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import com.example.nore.turndown.entity.dao.DaoSession;
import com.example.nore.turndown.entity.dao.ImageInfo;
import com.example.nore.turndown.entity.dao.Job;
import com.example.nore.turndown.entity.dao.Reporte;
import com.example.nore.turndown.entity.dao.TaskJob;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by NORE on 25/08/2015.
 */
public class CreteReportTask extends AsyncTask<Reporte, Void, Boolean> {

    public interface ReportBuilder {
        public void buildResult(boolean res);
    }

    private DaoSession session;
    private ReportBuilder res;
    private Context context;

    @Override
    protected Boolean doInBackground(Reporte... params) {

        if (session != null) {
            try {
                return buildPdfFile(params[0]);
            } catch (DocumentException e) {
                e.printStackTrace();
                return false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean buildPdfFile(Reporte t) throws DocumentException, IOException {
        Document document = new Document();

        File reporteDir = new File(Environment.getExternalStorageDirectory() + "/reportesMim");

        if (!reporteDir.exists()) {
            reporteDir.mkdir();
        }

        //try {
        PdfWriter.getInstance(document,
                new FileOutputStream(Environment.  //THIS WORKS
                        getExternalStorageDirectory() + "/reportesMim" + "/" +"linea_"+ t.getTrabajo() + "_" +"orden_"+ t.getSitio() + ".pdf"));

        document.open();

        Font font1 = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
        Font font2 = new Font(Font.FontFamily.HELVETICA, 12);
        Font font3 = new Font(Font.FontFamily.HELVETICA, 16);

        InputStream ims = context.getAssets().open("mimlogo.png");
        Bitmap bmp = BitmapFactory.decodeStream(ims);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image imageLogo = Image.getInstance(stream.toByteArray());
        document.add(imageLogo);

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        document.add(new Phrase("Usuario: ", font1));
        document.add(new Phrase(t.getUsuario2().getUsuario(), font2));

        document.add(new Phrase("  Nombre: ", font1));
        document.add(new Phrase(t.getUsuario2().getNombre() + " " + t.getUsuario2().getApellido(), font2));

        document.add(Chunk.NEWLINE);

        document.add(new Phrase("Linea: ", font1));
        document.add(new Phrase(t.getTrabajo() + "  ", font2));

        document.add(new Phrase("# Orden: ", font1));
        document.add(new Phrase(t.getSitio() + "  ", font2));

        //document.add(new Chunk(new LineSeparator()));
        //document.add(Chunk.NEWLINE);
        boolean control = true;
        for (Job jb : t.getJobs2()) {

            document.add(Chunk.NEWLINE);
            document.add(new Phrase("Descripcion: ", font1));
            document.add(new Phrase(jb.getJob(), font3));

            if (control) {
                document.add(Chunk.NEWLINE);
                control = false;
            }

            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Notas ", font1));
            List unorderedList = new List(List.UNORDERED);

            for (TaskJob ac : jb.getTasks2()) {
                if (ac != null) {
                    if (ac.getDescripcion() != null) {
                        if (!ac.getDescripcion().equals("Nota...")) {
                            unorderedList.add(new ListItem(ac.getDescripcion()));
                        }
                    }
                }
            }
            document.add(unorderedList);

            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Imagenes: ", font1));

            document.add(Chunk.NEWLINE);

            int columnas = 0;
            int recorre = 0;
            boolean add = true;
            if (jb.getImageInfo2() != null) {
                if (jb.getImageInfo2().size() >= 2) {
                    columnas = 2;
                } else {
                    columnas = 1;
                    add = false;
                }
            }

            PdfPTable table = new PdfPTable(columnas);
            table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

            if (jb.getImageInfo2() != null) {
                for (ImageInfo img : jb.getImageInfo2()) {
                    recorre++;
                    Image image = Image.getInstance(img.getImgRoute());
                    image.scaleAbsolute(150f, 150f);
                    PdfPCell cell2 = new PdfPCell(image);
                    cell2.setPadding(4);
                    table.addCell(cell2);
                }
            } else {
                PdfPCell cell2 = new PdfPCell(new Paragraph("no imagen"));
                table.addCell(cell2);
            }

            if ((recorre & 1) == 0) {
            } else {
                if (add) {
                    PdfPCell cell2 = new PdfPCell(new Paragraph(""));
                    table.addCell(cell2);
                }
            }

            document.add(table);

            document.add(new Chunk(new LineSeparator()));
            document.newPage();

        }

        document.close();
        return true;
        //} catch (Exception e) {
        //    return false;
        //}
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (res != null) {
            res.buildResult(aBoolean);
        }
    }

    public void setSession(DaoSession session) {
        this.session = session;
    }

    public void setRes(ReportBuilder res) {
        this.res = res;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
