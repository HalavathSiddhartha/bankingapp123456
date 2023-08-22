<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Forgot Password</title>
   
</head>
<body>
    <h1>Forgot Password</h1>
    <form name="forgotPasswordForm" onsubmit="return validateForm()" action="resetPassword" method="post">
        
        Account Number<input type="text" id="accountNumber" name="accountNumber" required><br>

      
        New PassWord<input type="password" id="newPassword" name="newPassword" required><br>

        
        Confirm Password<input type="password" id="confirmPassword" name="confirmPassword" required><br>

        <button type="submit">Reset Password</button>
        ${message}
        </form>
         <script>
        function validateForm() {
            var accountNumber = document.forms["forgotPasswordForm"]["accountNumber"].value;
            var newPassword = document.forms["forgotPasswordForm"]["newPassword"].value;
            var confirmPassword = document.forms["forgotPasswordForm"]["confirmPassword"].value;

            if (accountNumber === "") {
                alert("Please enter your account number");
                return false;
            }

            if (newPassword === "") {
                alert("Please enter a new password");
                return false;
            }

            if (confirmPassword === "") {
                alert("Please confirm your new password");
                return false;
            }

            if (newPassword !== confirmPassword) {
                alert("Passwords do not match");
                return false;
            }
        }
    </script>
    
</body>
</html>
