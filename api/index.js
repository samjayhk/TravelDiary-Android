require('dotenv').config()
const bcrypt = require ('bcrypt')

const server = require('./server')


server.listen('3001')