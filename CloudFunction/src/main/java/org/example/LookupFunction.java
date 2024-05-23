package org.example;

import com.google.cloud.compute.v1.*;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.cloud.functions.HttpFunction;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LookupFunction implements HttpFunction {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        String zone = request.getFirstQueryParameter("zone").orElse("");
        String project = request.getFirstQueryParameter("prjid").orElse("");
        String group = request.getFirstQueryParameter("group").orElse("");

        if (zone.isEmpty() || project.isEmpty() || group.isEmpty()) {
            response.setStatusCode(400);
            response.getWriter().write("{\"error\": \"Missing required query parameters\"}");
            return;
        }

        try {
            List<String> ips = getInstanceGroupIps(zone, project, group);
            response.setStatusCode(200);
            response.getWriter().write(new Gson().toJson(ips));
        } catch (IOException e) {
            response.setStatusCode(500);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private List<String> getInstanceGroupIps(String zone, String project, String group) throws IOException {
        List<String> ips = new ArrayList<>();
        System.out.println("==== Listing IPs of running VM from na instance group: " + group);
        try (InstancesClient client = InstancesClient.create()) {
            for (Instance curInst : client.list(project, zone).iterateAll()) {
                if (curInst.getName().contains(group)) {
                    String ip = curInst.getNetworkInterfaces(0).getAccessConfigs(0).getNatIP();
                    ips.add(ip);
                }
            }
        }
        return ips;
    }

}