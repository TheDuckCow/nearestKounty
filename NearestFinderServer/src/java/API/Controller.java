/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import test.Test;

/**
 *
 * @author fcarterwheatley
 */
public class Controller extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //API responds with JSON
        response.setContentType("application/json");
 
        try (PrintWriter out = response.getWriter()) {
            //Perform different operations based on the path that was requested
            JSONObject data = new JSONObject();
            switch (request.getRequestURI()) {
                case "/NearestFinderServer/getNearestKLocationsAtCounty":
                    data.put("ok", Test.getNearestKLocationsAtCounty());
                    break;
                case "/NearestFinderServer/getLocationsInBound":
                    data.put("ok", Test.getLocationsInBound());
                    break;
                case "/NearestFinderServer/getLocationsAtCounty":
                    data.put("ok", Test.getLocationsAtCounty());
                    break;
                case "/NearestFinderServer/getNearestKLocationsAtCoord":
                    data.put("ok", Test.getNearestKLocationsAtCoord());
                    break;
                case "/NearestFinderServer/isWithinBound":
                    int x = Integer.parseInt(request.getParameter("locationX"));
                    int y = Integer.parseInt(request.getParameter("locationY"));
                    data.put("mouseX", x);
                    data.put("mouseY", y);
                    data.put("ok", Test.isWithinBound(x, y));
                    break;
                default:
                    JSONArray endpoints =  new JSONArray();
                    endpoints.add("/NearestFinderServer/getNearestKLocationsAtCounty");
                    endpoints.add("/NearestFinderServer/getLocationsInBound");
                    endpoints.add("/NearestFinderServer/getLocationsAtCounty");
                    endpoints.add("/NearestFinderServer/getNearestKLocationsAtCoord");
                    endpoints.add("/NearestFinderServer/isWithinBound");
                    
                    data.put("error", "request not recognized");
                    data.put("available endpoints", endpoints);
                    break;
            }
            out.println(data);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Controller for the various API methods that are exposed.";
    }// </editor-fold>

}
