//Array.prototype.removeDuplicates = function() {
//	if ($scope.tags.indexOf(t) === -1) $scope.tags.push(t);
//};

var app = angular.module('Arxiver', ['ngRoute', 'ngResource']);

app.config(function($routeProvider) {

	$routeProvider
		.when('/upload', {
			templateUrl: 'views/upload.html',
			controller: 'Upload'
		})
		.when('/list', {
			templateUrl: 'views/list.html',
			controller: 'List'
		})
		.when('/', {
			templateUrl: 'views/home.html',
			controller: 'Home'
		})
		.otherwise({
			redirectTo: '/'
		});

});

app.service('SearchService', function($http, $rootScope, $resource) {
	return {
		search: function(criteria, fn) {
			var Search = new $resource('api/search');
			Search.query({tags: criteria}, function(data) {
				fn(data);
			});
		},

		upload: function(file, tags, sFn, eFn) {

			var formData = new FormData();			
			//formData.append('tags', tags);
			formData.append('file', file);

			var req = new XMLHttpRequest();
			req.onreadystatechange = function() {
				if (req.readyState == 4 && req.status == 200) {
					$rootScope.$apply(function() {
						sFn();
					});
				} else if (req.readyState == 4 && req.status != 200) {
					$rootScope.$apply(function() {
						eFn(req.status);
					});
				}
			};

			req.open("POST", "upload", true);
  			req.send(formData);
			
		},

		download: function(arxivId) {
			return $http.get('/arxiv/'+arxivId);
			/*
			var DownloadResource = $resource('/arxiv/:id', {
				'id': '@id'
			});

			DownloadResource.get({
				'id': arxivId
			}, sFn, eFn);
			*/
		}
	};
});

app.controller('Upload', function($scope, SearchService, $location) {

	$scope.init = function() {
		$scope.tags = [];
		var droparea = document.getElementById("droparea");
		droparea.addEventListener('drop', function(event) {
			console.log('on drop');
			event.stopPropagation();
        	event.preventDefault();

			$scope.$apply(function() {
				$scope.processFile(event.dataTransfer.files[0]);
			});        	
		}, false);
		droparea.addEventListener('dragover', function() {
			event.stopPropagation();
        	event.preventDefault();

        	droparea.className = droparea.className + " hover";
		}, false);

	};

	$scope.fileChanged = function() {
		$scope.$apply(function() {
			$scope.processFile(document.getElementById('file').files[0]);
		});
	};

	$scope.processFile = function(file) {
		$scope.file = {
			filename: file.name,
			size: file.size,
			type: file.type,
			file: file
		};
		$scope.tags = [];

		var tags = file.name.split(".");
		tags = tags.concat(file.name.split(":"));
		tags = tags.concat(file.name.split(","));
		tags = tags.concat(file.name.split(" "));

		// remove short words (<= 2)
		angular.forEach(tags, function(t) {
			var index = tags.indexOf(t);
			if (t.length <= 2) tags.splice(index, 1);
		});

		angular.forEach(tags, function(t) {
			if ($scope.tags.indexOf(t) === -1) $scope.tags.push(t);
		});
	};

	$scope.addTags = function(tagsToAdd) {
		angular.forEach(tagsToAdd.split(" "), function(t) {
				if ($scope.tags.indexOf(t) === -1) $scope.tags.push(t);
			});
	};

	$scope.removeTag = function(t) {
		var index = $scope.tags.indexOf(t);
		$scope.tags.splice(index, 1);
	}

	$scope.upload = function() {
		SearchService.upload($scope.file.file, $scope.tags,
			function() {
				$location.path('/');
			}, function(status) {
				$scope.error = {
					status: status,
					message: 'Upload failed'
				};
			});
	};
});

app.controller('List', function($scope, $rootScope, $http) {
	$scope.data = $rootScope.data;
});

app.controller('Home', function($scope, SearchService, $location) {
	$scope.search = function(criteria) {
		$scope.criteria = criteria;
		SearchService.search(criteria, function(data) {
			$scope.data = data;
		})
		/*
			.success(function(data, status) {
				
			})
			.error(function(data, status) {
				$scope.error = {
					data: data,
					status: status
				};
			});
			*/
	};

	$scope.download = function(arxiv) {

/*
		SearchService.download(arxiv._id)
			.success(function(data, status) {
				//console.log(data);
				window.open(data, '_blank');
			}).error(function(data, status) {
				$scope.error = {
					status: null,
					message: 'download error'
				};
			});
*/
	};
});

var areaFile