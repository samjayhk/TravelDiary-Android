
const mysqlServer = require('mysql')

const connection = mysqlServer.createConnection({
	host: process.env.MYSQL_HOST,
	user: process.env.MYSQL_USER,
	password: process.env.MYSQL_PASSWORD,
	database: process.env.MYSQL_DATABASE,
	port: process.env.MYSQL_PORT
})

const errorHandler = (error, msg, rejectFunction) => {
	console.error(error)
	rejectFunction({ error: msg })
}

const usersModule = require('../models/users')({ connection, errorHandler })
const threadModule = require('../models/thread')({ connection, errorHandler })

module.exports = {
	users: () => usersModule,
	threads: () => threadModule
}