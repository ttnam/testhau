require('../Model/ImageDAO')();

module.exports = function(app) { 
    var imageDAO = new ImageDAO();
    var url = '/api/images';    

    app.get(url + '/:imageId', function(req, res) {
        imageDAO.getById(req.params.imageId, function(result) {
            if (result == -1) {
                res.statusCode = 500;
                return res.send('Error 500: Get view server error.');
            }

            res.json(result); 
        });
    });
}
