package com.aa.safelocksaving.Operation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aa.safelocksaving.R;
import com.aa.safelocksaving.View_Reports_Activity;
import com.aa.safelocksaving.data.CardItem;
import com.aa.safelocksaving.data.Reminders_CardData;
import com.aa.safelocksaving.data.Reminders_ShopData;
import com.aa.safelocksaving.data.Reminders_SubscriptionData;
import com.aa.safelocksaving.data.Status;
import com.aa.safelocksaving.data.Type;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class ReportCardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CardItem> items;
    private Context context;
    private String filePath;

    public ReportCardListAdapter(List<CardItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public void createPDF() {
        Document document = new Document();
        filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + File.separator + context.getString(R.string.reportText) + "_test.pdf";
        try {
            Toast.makeText(context, filePath, Toast.LENGTH_SHORT).show();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            //Title
            Paragraph reportDocument = new Paragraph(String.format("%s\n\n", context.getString(R.string.reportText)), new Font(Font.FontFamily.UNDEFINED, 20.0f, Font.BOLD, BaseColor.GRAY));
            reportDocument.setAlignment(Element.ALIGN_CENTER);
            //Table
            PdfPTable table = new PdfPTable(6);

            //Title cell
            //Name
            PdfPCell cellName = new PdfPCell(new Paragraph(context.getString(R.string.nameText), new Font(Font.FontFamily.UNDEFINED, 15.0f, Font.BOLD)));
            cellName.setHorizontalAlignment(Element.ALIGN_CENTER);
            //Type
            PdfPCell cellType = new PdfPCell(new Paragraph(context.getString(R.string.typeText), new Font(Font.FontFamily.UNDEFINED, 15.0f, Font.BOLD)));
            cellType.setHorizontalAlignment(Element.ALIGN_CENTER);
            //Amount
            PdfPCell cellAmount = new PdfPCell(new Paragraph(context.getString(R.string.amountText), new Font(Font.FontFamily.UNDEFINED, 15.0f, Font.BOLD)));
            cellAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
            //Month
            PdfPCell cellMonth = new PdfPCell(new Paragraph(context.getString(R.string.monthsText), new Font(Font.FontFamily.UNDEFINED, 15.0f, Font.BOLD)));
            cellMonth.setHorizontalAlignment(Element.ALIGN_CENTER);
            //Year
            PdfPCell cellYear = new PdfPCell(new Paragraph(context.getString(R.string.yearText), new Font(Font.FontFamily.UNDEFINED, 15.0f, Font.BOLD)));
            cellYear.setHorizontalAlignment(Element.ALIGN_CENTER);
            //Status
            PdfPCell cellStatus = new PdfPCell(new Paragraph(context.getString(R.string.statusText), new Font(Font.FontFamily.UNDEFINED, 15.0f, Font.BOLD)));
            cellStatus.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(cellName);
            table.addCell(cellType);
            table.addCell(cellAmount);
            table.addCell(cellMonth);
            table.addCell(cellYear);
            table.addCell(cellStatus);

            for (int i = 0; i < items.size(); i++) {
                String name = items.get(i).getType() == Type.CARD ? ((Reminders_CardData) items.get(i).getItem()).getName() : items.get(i).getType() == Type.SUBSCRIPTION ? ((Reminders_SubscriptionData) items.get(i).getItem()).getName() : ((Reminders_ShopData) items.get(i).getItem()).getName();
                String type = items.get(i).getType() == Type.CARD ? context.getString(R.string.cardText) : items.get(i).getType() == Type.SUBSCRIPTION ? context.getString(R.string.subscriptionText) : context.getString(R.string.shopText);
                double amount = items.get(i).getType() == Type.CARD ? ((Reminders_CardData) items.get(i).getItem()).getSettlement() : items.get(i).getType() == Type.SUBSCRIPTION ? ((Reminders_SubscriptionData) items.get(i).getItem()).getAmount() : ((Reminders_ShopData) items.get(i).getItem()).getAmount();
                int month = items.get(i).getType() == Type.CARD ? ((Reminders_CardData) items.get(i).getItem()).getDeadline().getMonth() : items.get(i).getType() == Type.SUBSCRIPTION ? ((Reminders_SubscriptionData) items.get(i).getItem()).getDate().getMonth() : ((Reminders_ShopData) items.get(i).getItem()).getDeadline().getMonth();
                int year = items.get(i).getType() == Type.CARD ? ((Reminders_CardData) items.get(i).getItem()).getDeadline().getYear() : items.get(i).getType() == Type.SUBSCRIPTION ? ((Reminders_SubscriptionData) items.get(i).getItem()).getDate().getYear() : ((Reminders_ShopData) items.get(i).getItem()).getDeadline().getYear();
                String status = items.get(i).getStatus() == Status.DELETED ? context.getString(R.string.deletedText) : items.get(i).getStatus() == Status.ACTIVE ? context.getString(R.string.activeText) : items.get(i).getStatus() == Status.PAUSED ? context.getString(R.string.pausedText) : items.get(i).getStatus() == Status.FINISHED ? context.getString(R.string.finishedText) : items.get(i).getStatus() == Status.CANCELED ? context.getString(R.string.canceledText) : items.get(i).getStatus() == Status.INDETERMINATE ? context.getString(R.string.indeterminateText) : context.getString(R.string.limitText);

                table.addCell(new Paragraph(name));
                table.addCell(new Paragraph(type));
                table.addCell(new Paragraph(context.getString(R.string.moneyText) + amount));
                table.addCell(new Paragraph(month + ""));
                table.addCell(new Paragraph(year + ""));
                table.addCell(new Paragraph(status));
            }

            //Open, Add and Close
            if (document.isOpen()) document.close();
            document.open();
            document.addAuthor(context.getString(R.string.app_name));
            document.add(reportDocument);

            document.add(table);
            document.close();
            Toast.makeText(context, "Document Created", Toast.LENGTH_SHORT).show();
        } catch (DocumentException | FileNotFoundException e) {
            Toast.makeText(context, "Document error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void sharePDF() {
        createPDF();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        //intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, "Share Via"));

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.report_card_element,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CardItem item = items.get(position);
        ((CardViewHolder) holder).setCardBind(item);
    }

    @Override
    public int getItemCount() { return items.size(); }

    class CardViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final LinearLayout cardView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            cardView= itemView.findViewById(R.id.linearLayoutCard);
        }

        void setCardBind(@NonNull CardItem item) {
            String nameText = item.getType() == Type.CARD ?
                    ((Reminders_CardData)item.getItem()).getName() :
                    item.getType() == Type.SUBSCRIPTION ?
                            ((Reminders_SubscriptionData)item.getItem()).getName() :
                            item.getType() == Type.SHOP ?
                                    ((Reminders_ShopData)item.getItem()).getName() : "Unknown";
            int monthText = item.getType() == Type.CARD ?
                    ((Reminders_CardData)item.getItem()).getDeadline().getMonth() :
                    item.getType() == Type.SUBSCRIPTION ?
                            ((Reminders_SubscriptionData)item.getItem()).getDate().getMonth() :
                            item.getType() == Type.SHOP ?
                                    ((Reminders_ShopData)item.getItem()).getDeadline().getMonth() : 0;
            int yearText = item.getType() == Type.CARD ?
                    ((Reminders_CardData)item.getItem()).getDeadline().getYear() :
                    item.getType() == Type.SUBSCRIPTION ?
                            ((Reminders_SubscriptionData)item.getItem()).getDate().getYear() :
                            item.getType() == Type.SHOP ?
                                    ((Reminders_ShopData)item.getItem()).getDeadline().getYear() : 0;
            double amountText = item.getType() == Type.CARD ?
                    ((Reminders_CardData)item.getItem()).getAmount() :
                    item.getType() == Type.SUBSCRIPTION ?
                            ((Reminders_SubscriptionData)item.getItem()).getAmount() :
                            item.getType() == Type.SHOP ?
                                    ((Reminders_ShopData)item.getItem()).getAmount() : 0;
            String dateText = item.getType() == Type.CARD ?
                    ((Reminders_CardData)item.getItem()).getDeadline().fullDate() :
                    item.getType() == Type.SUBSCRIPTION ?
                            ((Reminders_SubscriptionData)item.getItem()).getDate().fullDate() :
                            item.getType() == Type.SHOP ?
                                    ((Reminders_ShopData)item.getItem()).getDeadline().fullDate() : "Unknown";
            title.setText(nameText);
            Intent intent = new Intent(context, View_Reports_Activity.class)
                    .putExtra("name", nameText)
                    .putExtra("month", monthText)
                    .putExtra("year", yearText)
                    .putExtra("amount", amountText)
                    .putExtra("date", dateText);

            cardView.setOnClickListener(view -> context.startActivity(intent));


        }
    }
}
