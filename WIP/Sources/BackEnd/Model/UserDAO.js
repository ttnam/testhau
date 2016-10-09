var config = require('../config');
var jwt = require('jsonwebtoken');
var passwordHash = require('password-hash');

module.exports = function() { 

	this.UserDAO = function() {
		this.collection = 'User';
	}

	UserDAO.prototype.isExist = function(userId, callback) {
	    database.collection(this.collection).findOne({ userId : userId }, function(err, reply) {
			try {
				if (err)
					throw err;
				callback(reply);
			}
			catch(err) {
			    callback(null);
			}
        });
	} 

	UserDAO.prototype.genToken = function(userId) {
		var secrectToken = new Buffer(config.SecrectToken, 'base64').toString('ascii');
		var token = jwt.sign(userId, secrectToken, {
			expiresIn: 86400
		});
		return token;
	}

	UserDAO.prototype.login = function(userId, password, callback) {
		var that = this;
	    database.collection(this.collection).findOne({ userId : userId }, function(err, reply) {
	    	try {
				if (err)
					throw err;

				if (reply == null) {
					callback(null);
					return;
				}

				if (passwordHash.verify(password, reply.password)) {
					var token = that.genToken({ userId : reply.userId });
					database.collection(that.collection).update({ _id : reply._id }, { $set:{ token : token } }, function(err, reply) {
						try{
							if (err)
								throw err;

							if (reply.result.ok == 1)
				            	callback(token);
				            else 
				            	callback(null);
						}
						catch(err) {
						    callback(-1);
						}	
					});
				}
				else 
					callback(null);
			}
			catch(err) {
			    callback(-1);
			}
        });		
	};

	UserDAO.prototype.signup = function(userId, password, callback) {
		var that = this;
		this.isExist(userId, function(user) {
			if (user != null) {
				callback(null);
				return;
			}

	    	var User = {
				userId : userId,
				password : passwordHash.generate(password),
				userName : userId,
				fbId : '',
				token : that.genToken({ userId : userId }),
				avatar : '', 
				about : {
					gender : '',
					timeRegister : new Date().getTime()
				},
				followers : [],
				isFolloweds : [],
				album : []
			};
			
		    database.collection(that.collection).insertOne(User, function(err, reply) {
		    	try {
					if (err)
						throw err;
					if (reply.result.ok == 1)
		            	callback(User.token);
		            else 
		            	callback(null);
		        }
				catch(err) {
				    callback(-1);
				}
	        });	
		});
	};

	UserDAO.prototype.checkExpireToken = function(token, callback) {
		var secrectToken = new Buffer(config.SecrectToken, 'base64').toString('ascii');
		jwt.verify(token, secrectToken, function(err, decoded) {	
		   	try{
		   		if (err) 
		   			throw err;
		   		callback(true, decoded.userId);
			}
			catch(err) {
				if (err.name == 'TokenExpiredError' || err.name == 'JsonWebTokenError')
					callback(false, null);
				else
					callback(-1, null);
			}
		});
	}

	UserDAO.prototype.update = function(userId, userName, avatar, gender, callback) {
		database.collection(this.collection).update({ userId : userId }, { $set:{ 
			userName : userName,
			avatar : avatar,
			'about.gender' : gender
		} }, function(err, reply) {
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

	UserDAO.prototype.getAlbum = function(userId, callback) {
		database.collection(this.collection).findOne({ userId : userId }, { '_id' : false, 'album' : true }, function(err, reply) {
			try {
				if (err)
					throw err;

	            callback(reply.album);
            }
			catch(err) {
				callback(-1);
			}
        });	
	}

	UserDAO.prototype.getFollowers = function(userId, callback) {
		database.collection(this.collection).findOne({ userId : userId }, { '_id' : false, 'followers' : true }, function(err, reply) {
			try {
				if (err)
					throw err;
	            callback(reply.followers);
            }
			catch(err) {
				callback(-1);
			}
        });	
	}

	UserDAO.prototype.getIsFolloweds = function(userId, callback) {
		database.collection(this.collection).findOne({ userId : userId }, { '_id' : false, 'isFolloweds' : true }, function(err, reply) {
			try {
				if (err)
					throw err;
	            callback(reply.isFolloweds);
			}
			catch(err) {
				callback(-1);
			}
        });	
		
	}

	UserDAO.prototype.follow = function(userId, userId_2, callback) {
		var that = this;
		this.isExist(userId_2, function(user) {
			if (user == null) {
				callback(false);
				return;
			}

			that.getFollowers(userId, function(followers){
				try {
					var follower =  {
			            userId : userId_2,
			            userName : user.userName,
			            avatar : user.avatar
			        };

			        if (followers.length > 0) 
						followers.push(follower);
					else 
						followers = [follower];

					database.collection(that.collection).update({ userId : userId }, { $set:{ followers : followers } }, function(err, reply) {
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
		});
	}
}
