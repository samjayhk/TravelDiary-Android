const db = require('../config/index.js')
const bcrypt = require('bcrypt')
const jwt = require('jsonwebtoken')
const fs = require('fs')

let SALT_ROUNDS = 2
let SECRET = 'eutvh435874vj577yvy37b87'
let hash = text => bcrypt.hash(text, SALT_ROUNDS)

const parseToken = async(token, result) => {
	try {
		const session = await jwt.verify(token, SECRET)
		return result(session)
	} catch (e) {
		return result(false)
	}
}

const routes = (server) => {

	server.get('/', function(req, res){
			var body = '<html><body>This is the API of Travel Web!</body></html>'
			res.writeHead(200, {
			'Content-Length': Buffer.byteLength(body),
			'Content-Type': 'text/html'
			});
			res.write(body)
			res.end()
	});

	server.post('/users/register', async (req, res, next) => {
		if (!req.is('application/json')) {
			return next(
				new errors.InvalidContentError("Expects 'application/json'"),
			);
		}
        const { username, password, email } = req.params
		try {
			res.send(await db.users().register(username, password, email))
		} catch( error) {
			res.send(error)
		}
		next()
    })
    
    server.post('/users/login', async (req, res, next) => {
		if (!req.is('application/json')) {
			return next(
				new errors.InvalidContentError("Expects 'application/json'"),
			);
		}
        const { username, password } = req.params
		try {
            //req.headers['x-token']
			res.send(await db.users().login(username, password))
		} catch( error) {
			res.send(error)
		}
		next()
	})
	
	server.put('/users/updatepassword', async (req, res, next) => {
		if (!req.is('application/json')) {
			return next(
				new errors.InvalidContentError("Expects 'application/json'"),
			);
		}
		const { oldPassword, newPassword } = req.params
		const token = req.headers['x-token']

		if (token) {
			parseToken(token, async(result) => {
				if (result) {
					try {
						res.send(await db.users().updatePassword(result.uid, oldPassword, newPassword))
					} catch( error) {
						res.send(error)
					}
				} else {
					res.send({resuls: false, message: 'Token expired.'})
				}
			})
		} else {
			res.send({resuls: false, message: 'Please login.'})
		}
		next()
    })
    
	server.get('/thread/:page', async (req, res, next) => {
		const { page } = req.params
		try {
			res.send(await db.threads().list(page, 0))
		} catch( error) {
			res.send(error)
		}
		next()
	})

	server.get('/thread/tag/:tid/:page', async (req, res, next) => {
		const { page, tid } = req.params
		try {
			res.send(await db.threads().list(page, tid))
		} catch( error) {
			res.send(error)
		}
		next()
	})

	server.get('/thread/:pid/:page', async (req, res, next) => {
		const { pid, page } = req.params
		try {
			res.send(await db.threads().content(pid, page))
		} catch( error) {
			res.send(error)
		}
		next()
	})

	server.get('/thread/tags', async (req, res, next) => {
		try {
			res.send(await db.threads().tags())
		} catch( error) {
			res.send(error)
		}
		next()
	})

	server.get('/search/:keywords/:page', async (req, res, next) => {
		const { keywords, page } = req.params
		var character = keywords;
    var ascii = "";
    for (var i = 0; i < character.length; i++) {
      var code = Number(character[i].charCodeAt(0));
      if (code > 127) {
        var charAscii = code.toString(16);
        charAscii = new String("0000").substring(charAscii.length, 4) + charAscii;
        ascii += "\\u" + charAscii
      } else {
        ascii += character[i];
      }
    }
		try {
			res.send(await db.threads().search(ascii.replace(/\\/g, '%' + String.fromCharCode(92)), page))
		} catch( error) {
			res.send(error)
		}
		next()
	})

	server.post('/thread/write', async (req, res, next) => {
		
		const { subject, content, tid } = req.params
		const token = req.headers['x-token']

		if (token) {
			parseToken(token, async(result) => {
				if (result) {
					try {
						res.send(await db.threads().writeThread(result.uid, tid, subject, content))
					} catch( error) {
						res.send(error)
					}
				} else {
					res.send({resuls: false, message: 'Token expired.'})
				}
			})
		} else {
			res.send({resuls: false, message: 'Please login.'})
		}
		next()
	})
	
	server.post('/thread/:pid/write', async (req, res, next) => {
		const { pid, comment } = req.params
		const token = req.headers['x-token']

		if (token) {
			parseToken(token, async(result) => {
				if (result) {
					try {
						res.send(await db.threads().writeComment(result.uid, pid, comment))
					} catch( error) {
						res.send(error)
					}
				} else {
					res.send({resuls: false, message: 'Token expired.'})
				}
			})
		} else {
			res.send({resuls: false, message: 'Please login.'})
		}
		next()
	})
	
	server.post('/upload', (req, res, next) => {
		var filename = ''
		for (var key in req.files) {
		  if (req.files.hasOwnProperty(key)) {
				if (req.files[key].size < 2000000) {
					filename = Date.now() + '_' + `${req.files[key].name}`
					fs.renameSync(req.files[key].path, `${__dirname}` + '/../server/uploads/' + filename)
				} else {
					res.send({result: false, message: 'Files was over the limit (2MB).'})
				}
		  }
		}
		res.send({result: true, message: 'Successfully upload files.', filename: filename})
		next()
	})

	server.put('/thread/:pid/update', (req, res, next) => {
		const { pid, subject, content, tid } = req.params
		const token = req.headers['x-token']

		if (token) {
			parseToken(token, async(result) => {
				if (result) {
					try {
						res.send(await db.threads().updateThread(result.uid, pid, tid, subject, content))
					} catch( error) {
						res.send(error)
					}
				} else {
					res.send({resuls: false, message: 'Token expired.'})
				}
			})
		} else {
			res.send({resuls: false, message: 'Please login.'})
		}
		next()
	})

	server.put('/thread/comment/:cid/update', (req, res, next) => {
		const { cid, comment } = req.params
		const token = req.headers['x-token']

		if (token) {
			parseToken(token, async(result) => {
				if (result) {
					try {
						res.send(await db.threads().updateComment(result.uid, cid, comment))
					} catch( error) {
						res.send(error)
					}
				} else {
					res.send({resuls: false, message: 'Token expired.'})
				}
			})
		} else {
			res.send({resuls: false, message: 'Please login.'})
		}
		next()
	})

	server.del('/thread/:pid/delete', (req, res, next) => {
		const { pid } = req.params
		const token = req.headers['x-token']
		
		if (token) {
			parseToken(token, async(result) => {
				if (result) {
					try {
						res.send(await db.threads().deleteThread(result.uid, pid))
					} catch( error) {
						res.send(error)
					}
				} else {
					res.send({resuls: false, message: 'Token expired.'})
				}
			})
		} else {
			res.send({resuls: false, message: 'Please login.'})
		}
		next()
	})

	server.del('/thread/comment/:cid/delete', (req, res, next) => {
		const { cid } = req.params
		const token = req.headers['x-token']

		if (token) {
			parseToken(token, async(result) => {
				if (result) {
					try {
						res.send(await db.threads().deleteComment(result.uid, cid))
					} catch( error) {
						res.send(error)
					}
				} else {
					res.send({resuls: false, message: 'Token expired.'})
				}
			})
		} else {
			res.send({resuls: false, message: 'Please login.'})
		}
		next()
	})
}

module.exports = routes
