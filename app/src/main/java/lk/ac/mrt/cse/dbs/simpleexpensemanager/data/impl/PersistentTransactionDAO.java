package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DB_Handler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private DB_Handler db_handler;
    public PersistentTransactionDAO(DB_Handler db_handler) {
        this.db_handler = db_handler;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = db_handler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_Handler.accountNo,accountNo);
        values.put(DB_Handler.expenseType,expenseType.name());
        values.put(DB_Handler.amount,amount);
        values.put(DB_Handler.date,new SimpleDateFormat("yyyy-MM-dd").format(date));

        db.insert(DB_Handler.tbl_transaction,null,values);
        db.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = db_handler.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DB_Handler.tbl_transaction,null);
        if(cursor.moveToFirst()) {
            do{
                Transaction transaction = new Transaction(
                        new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(1)),
                        cursor.getString(2),
                        ExpenseType.valueOf(cursor.getString(3)),
                        Double.parseDouble(cursor.getString(3)));
                transactionList.add(transaction);
            }while(cursor.moveToNext());
        }
        db.close();
//        cursor.close();
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = db_handler.getReadableDatabase();
        Cursor cursor = db.query(DB_Handler.tbl_transaction, null, null, null, null, null, null, limit + "");
        if(cursor.moveToFirst()) {
            do{
                Transaction transaction = new Transaction(
                        new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(1)),
                        cursor.getString(2),
                        ExpenseType.valueOf(cursor.getString(3)),
                        Double.parseDouble(cursor.getString(3)));
                transactionList.add(transaction);
            }while(cursor.moveToNext());
        }
        db.close();
//        cursor.close();
        return transactionList;
    }
}
