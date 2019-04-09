package com.ubiquigame.utility;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ubiquigame.controller.R;
import com.ubiquigame.controller.ViewControllerActivity;
import com.ubiquigame.network.ControllerConnection;

import java.util.ArrayList;

import ubiquigame.common.impl.Server;

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.ServerHolder> {
    private Context context;
    private ArrayList<Server> servers;

    static class ServerHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtIP, txtPlayers;
        ServerHolder(View itemView) {
            super(itemView);
            // ini all fields of each card
            txtName = itemView.findViewById(R.id.txtName);
            txtIP = itemView.findViewById(R.id.txtIP);
            txtPlayers = itemView.findViewById(R.id.txtPlayers);
        }

        void setDetails(Server server) {
            // fill fields
            txtName.setText(String.format(Utility.appLocale, "Servername: %s", server.getName()));
            txtIP.setText(String.format(Utility.appLocale, "Server IP: %s", server.getIP()));
            txtPlayers.setText(String.format(Utility.appLocale,"Players: %d/4", server.getPlayers().length));
        }
    }

    public ServerAdapter(Context context, ArrayList<Server> servers) {
        this.context = context;
        this.servers = servers;
    }

    @NonNull
    @Override
    public ServerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.server_card, parent, false);
        final ServerHolder holder = new ServerHolder(v);
        // connect on click
        holder.itemView.setOnClickListener(vl -> {
            if(!Utility.u.isConnecting()){
                Utility.u.setConnecting(true);
                new Thread(new ControllerConnection((servers.get(holder.getAdapterPosition()).getIP()), context)).start();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServerHolder holder, int position) {
        Server server = servers.get(position);
        holder.setDetails(server);
    }

    @Override
    public int getItemCount() {
        return servers.size();
    }
}