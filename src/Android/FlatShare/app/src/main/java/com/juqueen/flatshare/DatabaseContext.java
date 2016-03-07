package com.juqueen.flatshare;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.io.File;
import java.io.IOException;

/**
 * Created by juqueen on 2/8/2016.
 */


class DatabaseContext extends ContextWrapper {

    private static final String DEBUG_CONTEXT = "DatabaseContext";
    private static StringBuffer db_dir;
    private static Context _ctx;

    public DatabaseContext(Context base, String path) {
        super(base);
        this.db_dir = new StringBuffer(path);
        this._ctx = base;
    }

    @Override
    public File getDatabasePath(String name)
    {

        File db = null;

        File dataDirectory = directoryCheck(_ctx);

        //String path = _ctx.getFilesDir()+File.separator+db_dir+File.separator+name;
        if (name.indexOf(File.separatorChar) < 0) {
          db = new File(dataDirectory,name);
        }

        db_dir.setLength(0);
        db_dir.append(dataDirectory.getPath().toString());
        int seprator_index = db_dir.toString().lastIndexOf(File.separator);


        while(seprator_index>0)
        {
            if(!db_dir.toString().contains("/flatshare")){
                break;
            }

            seprator_index = db_dir.toString().lastIndexOf(File.separator);

            try {
                Runtime.getRuntime().exec( "chmod 771 "+ db_dir.toString() );
            } catch (IOException e) {
                e.printStackTrace();
            }
            db_dir = db_dir.delete(seprator_index,db_dir.length());
        }

        /*  String par = db.getParentFile().getAbsolutePath();

        if (!db.getParentFile().exists())
        {
            db.getParentFile().mkdirs();
        }
      */

        return db;

    }


    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return openOrCreateDatabase(name,_ctx.MODE_PRIVATE, factory);
    }


    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory)
    {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name).getAbsolutePath(), null);
        // SQLiteDatabase result = super.openOrCreateDatabase(name, mode, factory);
        if (Log.isLoggable(DEBUG_CONTEXT, Log.WARN))
        {
            Log.w(DEBUG_CONTEXT,
                    "openOrCreateDatabase(" + name + ",,) = " + result.getPath());
        }
        return result;
    }

    private File directoryCheck(Context ctx) {

        File dataDirectory = new File(ctx.getFilesDir()+db_dir.toString());
        if (!dataDirectory.exists()) {
            if (!dataDirectory.mkdirs()) {
                Log.e("ProfCre ", "Pobl");
                return null;
            }
        }
        return dataDirectory;


    }

}
