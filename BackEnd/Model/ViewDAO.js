var config = require('../config');
var jwt = require('jsonwebtoken');

module.exports = function() { 

	this.ViewDAO = function() {
		this.collection = 'View';
	} 

	ViewDAO.prototype.getAll = function(callback) {
	    database.collection(this.collection).find({}, { 
	    	'_id' : false, 
	    	viewId : true, 
	    	name : true, 
	    	location : true,
	    	active : true, 
	    	album : true 
	    }).toArray(function(err, reply) {
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

	ViewDAO.prototype.getById = function(viewId, callback) {
	    database.collection(this.collection).findOne({ viewId : viewId }, { '_id' : false }, function(err, reply) {
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

	ViewDAO.prototype.addComment = function(viewId, user, content, callback) {
		var that = this;
		if (user == null) {
			callback(false);
			return;
		}
		database.collection(this.collection).findOne({ viewId : viewId }, { '_id' : false, 'comments' : true }, function(err, reply) {
			try {
				if (err)
					throw err;

				database.collection(that.collection).update({ viewId : viewId }, 
					{ $push:{ comments : {
						userId : user.userId,
						avatar : user.avatar,	
						content : content,
						createdTime : new Date().getTime()
					} } }, function(err, reply) {
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
			}
			catch(err) {
				callback(-1);
			}
		});	
	}

	ViewDAO.prototype.deleteComment = function(viewId, user, createdTime, callback) {
		var that = this;
		if (user == null) {
			callback(false);
			return;
		}
		database.collection(this.collection).update({ viewId : viewId }, 
			{ $pull:{ comments : {  
				userId : user.userId,
				createdTime : createdTime
			} } }, function(err, reply) {
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
	}
}
