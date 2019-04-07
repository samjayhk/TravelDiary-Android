const restify = require('restify')
const server = restify.createServer()
const routes = require('../models/routes')
const cors = require('./cors')

server.pre(cors.preflight)
server.use(cors.actual)
server.use(restify.plugins.queryParser())
server.pre(restify.pre.sanitizePath())
server.use(restify.plugins.bodyParser({ mapParams: true, requestBodyOnGet: true }))

server.get('/uploads/*', restify.plugins.serveStatic({
    directory: __dirname,
  }))

routes(server)

module.exports = server