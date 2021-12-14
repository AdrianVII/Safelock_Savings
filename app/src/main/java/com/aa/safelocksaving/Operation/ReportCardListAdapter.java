package com.aa.safelocksaving.Operation;

import android.content.Context;
import android.content.Intent;
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
import com.aa.safelocksaving.data.Type;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class ReportCardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CardItem> items;
    private Context context;

    public ReportCardListAdapter(List<CardItem> items, Context context) {
        this.items = items;
        this.context = context;
    }
    public void createPDF() throws FileNotFoundException, DocumentException {
        Document document = new Document();
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/report.pdf";
        Toast.makeText(context, filePath, Toast.LENGTH_SHORT).show();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        //Title
        Paragraph reportDocument = new Paragraph("Report");
        reportDocument.setAlignment(Element.ALIGN_CENTER);
        //Table
        PdfPTable table = new PdfPTable(2);
        //Title cell

        Paragraph paragraphName = new Paragraph(context.getString(R.string.nameText));
        paragraphName.setAlignment(Element.ALIGN_MIDDLE);
        Paragraph paragraphType = new Paragraph(context.getString(R.string.typeText));
        paragraphType.setAlignment(Element.ALIGN_MIDDLE);
        table.addCell(paragraphName);
        table.addCell(paragraphType);
        for (int i = 0; i < items.size(); i++) {
            String name = items.get(i).getType() == Type.CARD ? ((Reminders_CardData)items.get(i).getItem()).getName() : items.get(i).getType() == Type.SUBSCRIPTION ? ((Reminders_SubscriptionData)items.get(i).getItem()).getName() : ((Reminders_ShopData)items.get(i).getItem()).getName();
            String type = items.get(i).getType() == Type.CARD ? context.getString(R.string.cardText) : items.get(i).getType() == Type.SUBSCRIPTION ? context.getString(R.string.subscriptionText) : context.getString(R.string.shopText);
            table.addCell(new Paragraph(name));
            table.addCell(new Paragraph(type));
        }

        //Open, Add and Close
        document.open();
        document.addAuthor(context.getString(R.string.app_name));
        document.add(reportDocument);
        document.add(table);
        document.close();
        Toast.makeText(context, "Document Created", Toast.LENGTH_SHORT).show();
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
