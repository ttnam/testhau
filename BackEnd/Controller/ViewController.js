require('../Model/ViewDAO')();

module.exports = function(app) { 
	var viewDAO = new ViewDAO();
	var url = '/api/views';	

	app.get(url, function(req, res) {
	    viewDAO.getAll(function(result) {
	    	if (result == -1) {
	    		res.statusCode = 500;
		    	return res.send('Error 500: Get all of views server error.');
	    	}

	    	res.json(result);
	    });
	});

	app.get(url + '/:viewId', function(req, res) {
	    viewDAO.getById(req.params.viewId, function(result) {
	    	if (result == -1) {
	    		res.statusCode = 500;
		    	return res.send('Error 500: Get view server error.');
	    	}

	    	res.json(result); 
	    });
	});
}