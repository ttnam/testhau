module.exports = function() { 

	this.ImageDAO = function() {
		this.collection = 'Image';
	} 

	ImageDAO.prototype.getById = function(imageId, callback) {
	    database.collection(this.collection).findOne({ imageId : imageId }, { '_id' : false }, function(err, reply) {
	    	try {
				if (err)
					throw err;
				
	            callback(reply);
            }
			catch(err) {
			    callback(-1);
			}
        });
	};

	ImageDAO.prototype.update = function(imageId, description, callback) {
	    database.collection(this.collection).update({ imageId : imageId }, { $set:{ description : description } }, function(err, reply) {
	    	try {
				if (err)
					throw err;
				if (reply.result.ok == 1) 
					callback(true);
				else 
					callback(false);
            }
			catch(err) {
			    callback(-1);
			}
        });
	};
}
