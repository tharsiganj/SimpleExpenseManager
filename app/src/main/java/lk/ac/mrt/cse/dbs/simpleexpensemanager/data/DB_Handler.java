package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DB_Handler extends SQLiteOpenHelper {
    private static final String db_name = "200640N";
    private static final int db_version = 7;

    public static final String tbl_account = "accounts";
    public static final String tbl_transaction = "transactions";

    public static final String accountNo = "accountNo";
    public static final String bankName = "bankName";
    public static final String accountHolderName = "accountHolderName";
    public static final String accountBalance = "accountBalance";

    public static final String transactionID = "transactionID";
    public static final String expenseType = "expenseType";
    public static final String amount = "amount";
    public static final String date = "date";

    public DB_Handler(Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + tbl_account + "("
                + accountNo+ " TEXT PRIMARY KEY," + bankName + " TEXT,"
                + accountHolderName + " TEXT," + accountBalance + " REAL" + ")");

        db.execSQL("CREATE TABLE " + tbl_transaction + "("
                + transactionID + " INTEGER PRIMARY KEY AUTOINCREMENT," + date + " TEXT," + accountNo + " TEXT,"
                + expenseType + " TEXT," + amount + " REAL," + "FOREIGN KEY(" + accountNo +
                ") REFERENCES "+ tbl_account +"(" + accountNo + ") )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS '"+tbl_account+"'");
        db.execSQL("DROP TABLE IF EXISTS '"+tbl_transaction+"'");
        onCreate(db);
    }

}
