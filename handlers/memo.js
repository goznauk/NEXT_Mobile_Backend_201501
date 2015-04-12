var Datastore = require('nedb');
var mongo = require('mongodb');
var fs = require('fs');

var DBServer = mongo.Server;
var Db = mongo.Db
var BSON = mongo.BSONPure;
var ObjectId = require('mongodb').ObjectID;

 
db = new Db('memoserver_db', new DBServer('localhost', 27017, {auto_reconnect: true}));
db.open(function (err, db) {
  if(!err) { console.log("app.js : Connected to 'memoserver_db' database"); }
});

exports.create = function(req, res) {
  var body = req.body;

  _insertMemo(body, function(error, result) {
    res.json({error:error, result:result});
  });
};

exports.read = function(req, res) {
  var where = req.query;

  _findMemo(where, function(error, results) {
//    res.json({error:error, results:results});
		
//		console.log(results);
		
		res.render('list', { title: "Memo List", memoArray: results });
  });
};

exports.update = function(req, res) {
  var where = req.query;
  var body = req.body;

  _updateMemo(where, body, function(error, results) {
    res.json({error: error, results: results});
  });
};

exports.remove = function(req, res) {
  var where = req.query;

  _removeMemo(where, function(error, results) {
    res.json({error: error, results: results});
  });
};


function _insertMemo(body, callback) {
  body = typeof body === 'string' ? JSON.parse(body) : body;

  var memo = {
		author: body.author,
		memo: body.memo,
		image: "/images/uploaded/dummy.png",
		date: new Date()
	};

	db.collection('memos', function(err, collection) {
		collection.insert(memo, {safe:true}, function(err, result) {
			if (err) {
				console.log("ERROR : " + err);
				callback(err);
			} else {
				console.log('Success: ' + JSON.stringify(result));
				callback(null, result);
			}
		});
	});
}

exports.newMemo = function(memo) {
	if(memo) {
		db.collection('memos', function(err, collection) {
			collection.insert(memo, {safe:true}, function(err, result) {
				if (err) {
					console.log("ERROR : " + err);
					//callback(err)
				} else {
					console.log('Success: ' + JSON.stringify(result));
				//	callback(null, result);
				}
			});
		});
	}
}



function _findMemo(where, callback){
	where = where || {};
	db.collection('memos', function(err, collection) {
		collection.find(where).toArray(function(err, items) {
			if (err) {
				console.log("ERROR : " + err);
				callback(err);
			} else {
				console.log('Success: ' + JSON.stringify(items));
				callback(null, items);
			}
		});
	});
}

exports.findById = function(req, res) {
  var id = req.params.id;
  console.log('Retrieving memo: ' + id);
  db.collection('memos', function(err, collection) {
    collection.findOne({'_id': new ObjectId(id)}, function(err, memo) {
			res.render('memo', { title: "Memo id : " + id, item: memo });
		//	console.log(item);
      //res.send(item);
    });
  });
};

function _updateMemo(where, body, callback){
	body = typeof body === 'string' ? JSON.parse(body) : body;

	db.collection('memos', function(err, collection) {
		collection.update(where, {$set: body}, {multi:true, safe:true}, function(err, result) {
			if (err) {
				console.log("ERROR : " + err);
				callback(err);
			} else {
				console.log('Success: ' + JSON.stringify(result));
				callback(null, result);
			}
		});
	});
}

function _removeMemo(where, callback){
	db.collection('memos', function(err, collection) {
		collection.remove(where, {multi:true, safe:true}, function(err, result) {
			if (err) {
				console.log("ERROR : " + err);
				callback(err);
			} else {
				console.log('Success: ' + JSON.stringify(result));
				callback(null, result);
			}
		});
	});
}
