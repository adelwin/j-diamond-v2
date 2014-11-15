<!doctype html>
<html>
<head>
	<meta charset="utf-8">
	<title>Adelwin</title>
	<meta name="description" content="Welcome to my basic template.">
	<link rel="stylesheet" href="css/style.css">
</head>

<body>
<!-- Adding the ng-app declaration to initialize AngularJS -->
	<title>JDiamond V2 Expense Tracker</title>
	<div id="main" ng-app ng-controller="MainController">
		<nav>
			<a href="#" ng-repeat="menu in menus" ng-click="menuClick(menu)" ng-class="{active:menu.active}">
				{{menu.name}} | {{menu.active}}
			</a>
		</nav>
		<br>
		<p>You chose <b>{{menus[index].name}}</b></p>
	</div>
	<script src="js/angular.min.js"></script>
	<script src="js/controller.js"></script>
</body>
</html>