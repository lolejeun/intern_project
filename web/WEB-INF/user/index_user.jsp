<%-- 
    Document   : index_user
    Created on : 3 avr. 2019, 14:21:39
    Author     : Josselin
--%>
<%@page import="java.util.Hashtable"%>
<%@page import="model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Intern Project</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

    </head>

    <body>
        <h1 style="text-align: center; margin: 40px">Liste des utilisateurs</h1>
        <div class="row">
            <div class="card offset-sm-2 col-sm-8" style="width: 18rem;">
                <div class="card-body">
                    <table class="table">
                        <thead class="thead-dark">
                            <tr>
                                <th scope="col">#</th>
                                <th scope="col">Nom</th>
                                <th scope="col">Prénom</th>
                                <th scope="col">Email</th>
                                <th scope="col">Télephone</th>
                                <th scope="col">Type</th>
                                <th scope="col">Entreprise</th>
                                <th scope="col">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                Hashtable<Integer, User> usersTable=  (Hashtable<Integer, User>)request.getAttribute("Users");
                                for (int i=0; i< usersTable.size(); i++){
                                    out.println("<tr>");
                                    out.println("<td scope='row'>" + i + "</td>");
                                    out.println("<td>" + usersTable.get(i).getName() + "</td>");
                                    out.println("<td>" + usersTable.get(i).getFirst_name() + "</td>");
                                    out.println("<td>" + usersTable.get(i).getEmail() + "</td>");
                                    out.println("<td>" + usersTable.get(i).getPhone() + "</td>");
                                    if (usersTable.get(i).getIs_admin()){
                                        out.println("<td>Administrateur</td>");
                                    } else {
                                        out.println("<td>Stagiaire</td>");
                                    }
                                    out.println("<td>" + usersTable.get(i).getCompany().getName() + "</td>");
                                    out.println("<td><button class='btn btn-info'>Editer</button>");
                                    out.println("<button class='btn btn-info'>Supprimer</button></td>");
                                    out.println("</tr>");
                                }
                             %> 
                        </tbody>
                    </table>
                </div>
            </div>
        </div>			
    </body>
</html>

