<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Registration</title>
</head>
<body>
<form action="" method="post">
	<label>
		<input id="email" name="email" type="text" placeholder="Write your email..."
			   value="${(userDto.email)!}">
		${(problems.email)!}
	</label><br>
	<label>
		<input id="password" name="password" type="text" placeholder="Write your password..."
			   value="${(userDto.password)!}">
        ${(problems.password)!}
	</label><br>
	<label>
		<input id="matchingPassword" name="matchingPassword" type="text" placeholder="Repeat your password..."
			   value="${(userDto.matchingPassword)!}">
        ${(problems.matchingPassword)!}
	</label><br>
	<label>
		<input id="username" name="username" type="text" placeholder="Write your username..."
			   value="${(userDto.username)!}">
        ${(problems.username)!}
	</label><br>
	<input type="submit" value="Register">
    ${(problems.error)!}
</form>
</body>
</html>