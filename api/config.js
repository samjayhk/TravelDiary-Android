'use strict'

var mysql = require('mysql');

module.exports = {
    name: 'rest-api',
    hostname : '127.0.0.1',
    version: '0.0.1',
    env: process.env.NODE_ENV || 'development',
    port: process.env.PORT || 3307,
    db: {
        get : mysql.createConnection({
			host     : '1ocalhost',
			user     : 'root',
			password : 'usbw',
			database : 'travel',
			insecureAuth: true
		})
    }
}