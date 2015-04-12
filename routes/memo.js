var express = require('express');
var router = express.Router();

var formidable = require('formidable');
var http = require('http');
var fs = require('fs');
var path = require('path');

var memo = require('../handlers/memo');

router.get('/new', function(req, res, next) {
  res.render('new', { title: 'Add Memo' }); 
});

var uploadPath = '/var/www/simpleNodeServer/public/images/uploaded';

router.post('/new/upload', function(req, res) {
	var files = [],
			fields = [];

  var form = new formidable.IncomingForm();
	form.uploadDir = uploadPath;
	form.keepExtensions = true;
	form.multiples = true;

	console.log('upload Post');

  form.on('field', function(field, value) {
		console.log(field, value);
		fields.push([field, value]);
	})
	.on('file', function(field, file) {
		console.log(field, file);
		files.push([field, file]);
	})
	.on('fileBegin', function(name, file) {
    console.log('fileBegin - ' + name + ':' + JSON.stringify(file));
  })
  .on('progress', function(bytesReceived, bytesExpected) {
    console.log('progress: ' + bytesReceived + '/' + bytesExpected);
  })
  .on('aborted', function() {
    console.log('aborted');
  })
  .on('error', function(err) {
    console.log('error(formidable): ' + err);
  })
  .on('end', function() {
    console.log('end');
  });

	console.log(req.body.image);

  form.parse(req, function(err, fields, files) {
    console.log('parse - ' + JSON.stringify(files));
 //   res.writeHead(200, {'content-type': 'text/plain'});

		var resultObj = {
			result: 'ok',
			added: []
		}

		if(files.image.length > 1) {
			files.image.forEach(function(i) {
				resultObj.added.push({
					size: i.size,
					path: i.path,
					name: i.name,
					type: i.type
				});
			});
		} else {
			resultObj.added.push({
				size: files.image.size,
				path: files.image.path,
				name: files.image.name,
				type: files.image.type
			});
		}
//	console.log(JSON.stringify(resultObj));

		console.log("Upload completed" );
		console.log(fields);
		console.log(files);

		var resultMemo = {
			author: fields.author,
			memo: fields.memo,
			image: [],
			date: new Date()
		}
		
		if(files.image.length > 1) {
			files.image.forEach(function(i) {
				var elem = i.path.split("/");
				var str = "";
				for (var i = 0; i < elem.length; i++)
					str = elem[i];
				resultMemo.image.push("/images/uploaded/"+str);
			});
		} else {
			var elem = files.image.path.split("/");
			var str = "";
			for (var i = 0; i < elem.length; i++)
				str = elem[i];
			resultMemo.image.push("/images/uploaded/"+str);
		}


		memo.newMemo(resultMemo);

		console.log('dd' + resultMemo);
		res.redirect('/memo')
  });
});

router.post('/new', memo.create);
router.get('/', memo.read);
router.get('/:id', memo.findById);
router.put('/', memo.update);
router.delete('/', memo.remove);

module.exports = router;
