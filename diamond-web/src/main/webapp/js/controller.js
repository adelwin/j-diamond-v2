/*
 * File Name	: controller.js
 * Author		: Adelwin Handoyo
 * Created Date	: 13 May 2014 17:00
 *
 * Copyright (c) 2014 Solveware Independent. All Rights Reserved. <BR/>
 * <BR/>
 * This software contains confidential and proprietary information of Solveware Independent.<BR/>
 */

/**
 * Created by adelwin on 13/05/2014.
 */

function MainController($scope){

	$scope.menus = [
		{
			name: 'Home',
			active:true
		},{
			name: 'Entry',
			active:false
		},{
			name: 'History',
			active:false
		},{
			name: 'About',
			active:false
		}
	];

	$scope.index = 0;

	$scope.menuClick = function(clickedMenu){
		var clickedIndex = $scope.menus.indexOf(clickedMenu);
		$scope.index = clickedIndex;
		$scope.menus[0].active = false;
		$scope.menus[1].active = false;
		$scope.menus[2].active = false;
		$scope.menus[3].active = false;
		clickedMenu.active = true;
	};
}