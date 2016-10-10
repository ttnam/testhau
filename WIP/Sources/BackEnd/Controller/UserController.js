require('../Model/UserDAO')();
require('../Model/ImageDAO')();
require('../Model/ViewDAO')();

module.exports = function(app) { 
	var userDAO = new UserDAO();
	var imageDAO = new ImageDAO();
	var viewDAO = new ViewDAO();
	var url = '/api/users';	

	app.post(url + '/login', function(req, res) {
		if(!req.body.hasOwnProperty('userId') || !req.body.hasOwnProperty('password')) {
		    res.statusCode = 400;
		    return res.send('Error 400: Login syntax incorrect.');
		}

	    userDAO.login(req.body.userId, req.body.password, function(result) {
	    	if (result == -1) {
	    		res.statusCode = 500;
		    	return res.send('Error 500: Login server error.');
	    	}

	    	res.json(result);
	    });
	});

	app.post(url + '/signup', function(req, res) {
		if(!req.body.hasOwnProperty('userId') || !req.body.hasOwnProperty('password')) {
		    res.statusCode = 400;
		    return res.send('Error 400: SignUp syntax incorrect.');
		}

	    userDAO.signup(req.body.userId, req.body.password, function(result) {
	    	if (result == -1) {
	    		res.statusCode = 500;
		    	return res.send('Error 500: Login server error.');
	    	}

	    	res.json(result);
	    });
	});

	app.post(url + '/token', function(req, res) {
		if (!req.body.hasOwnProperty('token')) {
		    res.statusCode = 400;
		    return res.send('Error 400: Add comment syntax incorrect.');
		}

		userDAO.checkExpireToken(req.body.token, function(result, userId) {
			if (result == -1) {
	    		res.statusCode = 500;
		    	return res.send('Error 500: Check expire token server error.');
	    	}

	    	res.json(result);
		});
	});

	app.post(url + '/album', function(req, res) {
		if (!req.body.hasOwnProperty('token')) {
		    res.statusCode = 400;
		    return res.send('Error 400: Add comment syntax incorrect.');
		}

		userDAO.checkExpireToken(req.body.token, function(result, userId) {
			if (result == -1) {
	    		res.statusCode = 500;
		    	return res.send('Error 500: Check expire token server error.');
	    	}

	    	if (!result) {
	    		res.statusCode = 401;
		    	return res.send('Error 401: Token expries.');
	    	}

	    	userDAO.getAlbum(userId, function(result) {
				if (result == -1) {
		    		res.statusCode = 500;
			    	return res.send('Error 500: Get album of user server error.');
		    	}

		    	res.json(result);
			});
		});
	});

	app.post(url + '/followers', function(req, res) {
		if (!req.body.hasOwnProperty('token')) {
		    res.statusCode = 400;
		    return res.send('Error 400: Add comment syntax incorrect.');
		}

		userDAO.checkExpireToken(req.body.token, function(result, userId) {
			if (result == -1) {
	    		res.statusCode = 500;
		    	return res.send('Error 500: Check expire token server error.');
	    	}

	    	if (!result) {
	    		res.statusCode = 401;
		    	return res.send('Error 401: Token expries.');
	    	}

	    	userDAO.getFollowers(userId, function(result) {
				if (result == -1) {
		    		res.statusCode = 500;
			    	return res.send('Error 500: Get followers server error.');
		    	}

		    	res.json(result);
			});
		});
	});

	app.post(url + '/isFolloweds', function(req, res) {
		if (!req.body.hasOwnProperty('token')) {
		    res.statusCode = 400;
		    return res.send('Error 400: Add comment syntax incorrect.');
		}

		userDAO.checkExpireToken(req.body.token, function(result, userId) {
			if (result == -1) {
	    		res.statusCode = 500;
		    	return res.send('Error 500: Check expire token server error.');
	    	}

	    	if (!result) {
	    		res.statusCode = 401;
		    	return res.send('Error 401: Token expries.');
	    	}

	    	userDAO.getIsFolloweds(userId, function(result) {
				if (result == -1) {
		    		res.statusCode = 500;
			    	return res.send('Error 500: Get isFolloweds server error.');
		    	}

		    	res.json(result);
			});
		});
	});

	app.post(url + '/comments/:viewId', function(req, res) {
		if(!req.body.hasOwnProperty('token') || !req.body.hasOwnProperty('content')) {
		    res.statusCode = 400;
		    return res.send('Error 400: Add comment syntax incorrect.');
		}

	    userDAO.checkExpireToken(req.body.token, function(result, userId) {
			if (result == -1) {
	    		res.statusCode = 500;
		    	return res.send('Error 500: Check expire token server error.');
	    	}

	    	if (!result) {
	    		res.statusCode = 401;
		    	return res.send('Error 401: Token expries.');
	    	}

	    	userDAO.isExist(userId, function(user) {
				viewDAO.addComment(req.params.viewId, user, req.body.content, function(result) {
					if (result == -1) {
			    		res.statusCode = 500;
				    	return res.send('Error 500: Post comment server error.');
			    	}

			    	res.json(result);
				});
			});
		});
	});

	app.put(url, function(req, res) {
		if (!req.body.hasOwnProperty('token') && !req.body.hasOwnProperty('userName') 
			&& !req.body.hasOwnProperty('avatar') && !req.body.hasOwnProperty('gender')) {
		    res.statusCode = 400;
		    return res.send('Error 400: Add comment syntax incorrect.');
		}

		userDAO.checkExpireToken(req.body.token, function(result, userId) {
			if (result == -1) {
	    		res.statusCode = 500;
		    	return res.send('Error 500: Check expire token server error.');
	    	}

	    	if (!result) {
	    		res.statusCode = 401;
		    	return res.send('Error 401: Token expries.');
	    	}

	    	userDAO.update(userId, req.body.userName, req.body.avatar, req.body.gender, function(result) {
				if (result == -1) {
		    		res.statusCode = 500;
			    	return res.send('Error 500: Update info user server error.');
		    	}

		    	res.json(result);
			});
		});
	});

	app.put(url + '/follows/:userId', function(req, res) {
		if (!req.body.hasOwnProperty('token')) {
		    res.statusCode = 400;
		    return res.send('Error 400: Add comment syntax incorrect.');
		}

		userDAO.checkExpireToken(req.body.token, function(result, userId) {
			if (result == -1) {
	    		res.statusCode = 500;
		    	return res.send('Error 500: Check expire token server error.');
	    	}

	    	if (!result) {
	    		res.statusCode = 401;
		    	return res.send('Error 401: Token expries.');
	    	}

	    	userDAO.follow(userId, req.params.userId, function(result) {
				if (result == -1) {
		    		res.statusCode = 500;
			    	return res.send('Error 500: Follow user server error.');
		    	}

		    	res.json(result);
			});
		});
	});

	app.put(url + '/album/:imageId', function(req, res) {
		if (!req.body.hasOwnProperty('token') && !req.body.hasOwnProperty('description')) {
		    res.statusCode = 400;
		    return res.send('Error 400: Add comment syntax incorrect.');
		}

		userDAO.checkExpireToken(req.body.token, function(result, userId) {
			if (result == -1) {
	    		res.statusCode = 500;
		    	return res.send('Error 500: Check expire token server error.');
	    	}

	    	if (!result) {
	    		res.statusCode = 401;
		    	return res.send('Error 401: Token expries.');
	    	}

	    	imageDAO.update(req.params.imageId, req.body.description, function(result) {
				if (result == -1) {
		    		res.statusCode = 500;
			    	return res.send('Error 500: Update image server error.');
		    	}

		    	res.json(result);
			});
		});
	});

	app.delete(url + '/comments/:viewId', function(req, res) {
		if(!req.body.hasOwnProperty('token') || !req.body.hasOwnProperty('createdTime')) {
		    res.statusCode = 400;
		    return res.send('Error 400: Add comment syntax incorrect.');
		}

	    userDAO.checkExpireToken(req.body.token, function(result, userId) {
			if (result == -1) {
	    		res.statusCode = 500;
		    	return res.send('Error 500: Check expire token server error.');
	    	}

	    	if (!result) {
	    		res.statusCode = 401;
		    	return res.send('Error 401: Token expries.');
	    	}

	    	userDAO.isExist(userId, function(user) {
				viewDAO.deleteComment(req.params.viewId, user, req.body.createdTime, function(result) {
					if (result == -1) {
			    		res.statusCode = 500;
				    	return res.send('Error 500: Delete comment user server error.');
			    	}

			    	res.json(result);
				});
			});
		});
	});
}