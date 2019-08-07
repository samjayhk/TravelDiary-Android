const bcrypt = require('bcrypt')
const jwt = require('jsonwebtoken')

let SALT_ROUNDS = 2
let SECRET = 'eutvh435874vj577yvy37b87'
let hash = text => bcrypt.hash(text, SALT_ROUNDS)

const thread = deps => {
    return {
        list: (page, tid) => {
            return new Promise((resolve, reject) => {
                const { connection, errorHandler } = deps
                if (tid == 0) {
                    connection.query("SELECT p.pid, p.uid, u.username, t.name AS tag, p.subject, p.ptime, p.uptime, p.rate, t.name AS tags, COUNT(c.pid) AS count, CEIL((COUNT(c.pid))/8) AS pages, MAX(c.ctime) AS lcom, substr(substr(p.content, locate(?, p.content) + 5), 1, locate(?, substr(p.content, locate(?, p.content) + 5)) - 1) as cover, (? - p.ptime) AS plast, GREATEST(IFNULL(MAX(c.ctime), 0), p.ptime) AS ntime FROM comment c RIGHT JOIN post p ON p.pid = c.pid INNER JOIN tags t ON p.tid = t.tid INNER JOIN users u ON u.uid = p.uid GROUP BY p.pid ORDER BY ntime DESC LIMIT ?, ?", ['src="','"', 'src="', Date.now(), (parseInt(page)*10)-10, 10], (error, results) => {
						if (error) {
							console.log(error)
						} else {
							if (results.length >= 1) {
								connection.query('SELECT COUNT(pid) AS sum FROM post', (error, sumResults) => {
									resolve({result: true, page: parseInt(page), sum: Math.ceil(parseInt(sumResults[0].sum)/8), thread: results})
								})
							} else {
								resolve({result: false, message: 'No others thread found.'})
							}
						}
                    })
                } else {
                    connection.query('SELECT p.pid, p.uid, u.username, t.name AS tag, p.subject, p.ptime, p.uptime, p.rate, t.name AS tags, COUNT(c.pid) AS count, CEIL((COUNT(c.pid))/8) AS pages, MAX(c.ctime) AS lcom, substr(substr(p.content, locate(?, p.content) + 5), 1, locate(?, substr(p.content, locate(?, p.content) + 5)) - 1) as cover, (? - p.ptime) AS plast, GREATEST(IFNULL(MAX(c.ctime), 0), p.ptime) AS ntime FROM comment c RIGHT JOIN post p ON p.pid = c.pid INNER JOIN tags t ON p.tid = t.tid INNER JOIN users u ON u.uid = p.uid WHERE p.tid = ? GROUP BY p.pid ORDER BY ntime DESC LIMIT ?, ?', ['src="','"', 'src="', Date.now(), tid, (parseInt(page)*8)-8, 8], (error, results) => {
                        if (results.length >= 1) {
                            connection.query('SELECT COUNT(pid) AS sum FROM post', (error, sumResults) => {
                                resolve({result: true, page: parseInt(page), sum: Math.ceil(parseInt(sumResults[0].sum)/8), thread: results})
                            })
                        } else {
                            resolve({result: false, message: 'No others thread found.'})
                        }
                    })
                }
            })
        },
        content: (pid, page) => {
            return new Promise((resolve, reject) => {
                const { connection, errorHandler } = deps
                connection.query('SELECT p.pid, p.uid, u.username, f.avatar, p.subject, p.content, p.ptime, p.uptime, COUNT(c.pid) AS count, CEIL((COUNT(c.pid))/8) AS sum, p.tid, t.name AS tags FROM post p, comment c, users u, usersinfo f, tags t WHERE p.pid = ? AND p.uid = u.uid AND p.uid = f.uid AND p.pid = c.pid AND t.tid = p.tid ORDER BY p.ptime DESC', [parseInt(pid)], (error, thread_result) => {
                    if (thread_result.length >= 1) {
                            connection.query('SELECT c.cid, c.pid, c.uid, u.username, f.avatar, c.comment, c.ctime, c.uptime FROM comment c, users u, usersinfo f WHERE c.pid = ? AND c.uid = u.uid AND f.uid = c.uid ORDER BY c.cid ASC LIMIT ?, ?', [parseInt(pid), (parseInt(page)*8)-8, 8], (error, comment_result) => {
                                if (parseInt(page) > 1) {
                                    if (comment_result.length >= 1) {
                                        resolve({ result: true, subject: thread_result[0].subject, page: parseInt(page), sum: thread_result[0].sum, comment: comment_result})
                                    } else {
                                        resolve({ result: false, message: 'No others comment found.'})
                                    }
                                } else {
                                    if (comment_result[0] != null){
                                        resolve({ result: true, subject: thread_result[0].subject, page: parseInt(page), sum: thread_result[0].sum, thread: thread_result, comment: comment_result})
                                    } else {
                                        resolve({ result: true, subject: thread_result[0].subject, page: parseInt(page), sum: thread_result[0].sum, thread: thread_result, comment: null})
                                    }
                                }
                            })
                    } else {
                        resolve({result: false, message: 'Thread not found or deleted.'})
                    }
                })
            })
        },
        writeThread: (uid, tid, subject, content) => {
            return new Promise((resolve, reject) => {
                const { connection, errorHandler } = deps
                connection.query('INSERT INTO post (uid, subject, content, ptime, uptime, tid) VALUES (?, ?, ?, ?, ?, ?)', [uid, subject, content, Date.now(), Date.now(), tid], (error, results) => {
                    if (error) {
                        errorHandler(error, 'Something went wrong.', reject)
                        resolve({result: false, message: 'Unsuccessfully wrote thread.'})
                    }
                    resolve({result: true, message: 'Successfully wrote thread.'})
                })
            })
        },
        writeComment: (uid, pid, comment) => {
            return new Promise((resolve, reject) => {
                const { connection, errorHandler } = deps
                connection.query('INSERT INTO comment (uid, pid, comment, ctime, uptime) VALUES (?, ?, ?, ?, ?)', [uid, pid, comment, Date.now(), Date.now()], (error, results) => {
                    if (error) {
                        errorHandler(error, 'Something went wrong.', reject)
                        resolve({result: false, message: 'Unsuccessfully wrote comment.'})
                    }
                    resolve({result: true, message: 'Successfully wrote comment.'})
                })
            })
        },
        tags: () => {
            return new Promise((resolve, reject) => {
                const { connection, errorHandler } = deps
                connection.query('SELECT * from tags ORDER BY tid', (error, results) => {
                    if (error) {
                        errorHandler(error, 'Something went wrong.', reject)
                        resolve({result: false, message: 'Something went wrong.'})
                    }
                    resolve({result: true, tags: results})
                })
            })
        },
        updateThread: (uid, pid, tid, subject, content) => {
            return new Promise((resolve, reject) => {
                const { connection, errorHandler } = deps
                connection.query('SELECT * from post WHERE pid = ? AND uid = ?', [pid, uid], (error, threadResults) => {
                    if (error) {
                        errorHandler(error, 'Something went wrong.', reject)
                        resolve({result: false, message: 'Something went wrong.'})
                    }
                    if (threadResults.length >= 1) {
                        connection.query('UPDATE post SET tid = ?, subject = ?, content = ?, uptime = ? WHERE pid = ?', [tid, subject, content, Date.now(), pid], (error, threadResults) => {
                            if (error) {
                                errorHandler(error, 'Something went wrong.', reject)
                                resolve({result: false, message: 'Something went wrong.'})
                            }
                            resolve({result: true, message: 'Successfully updated thread.'})
                        })
                    } else {
                        resolve({result: false, message: 'Thread not found or No permission.'})
                    }
                })
            })
        },
        updateComment: (uid, cid, comment) => {
            return new Promise((resolve, reject) => {
                const { connection, errorHandler } = deps
                connection.query('SELECT * from comment WHERE cid = ? AND uid = ?', [cid, uid], (error, threadResults) => {
                    if (error) {
                        errorHandler(error, 'Something went wrong.', reject)
                        resolve({result: false, message: 'Something went wrong.'})
                    }
                    if (threadResults.length >= 1) {
                        connection.query('UPDATE comment SET comment = ?, uptime = ? WHERE cid = ?', [comment, Date.now(), cid], (error, threadResults) => {
                            if (error) {
                                errorHandler(error, 'Something went wrong.', reject)
                                resolve({result: false, message: 'Something went wrong.'})
                            }
                            resolve({result: true, message: 'Successfully updated comment.'})
                        })
                    } else {
                        resolve({result: false, message: 'Comment not found or No permission.'})
                    }
                })
            })
        },
        deleteThread: (uid, pid) => {
            return new Promise((resolve, reject) => {
                const { connection, errorHandler } = deps
                connection.query('SELECT * from post WHERE pid = ? AND uid = ?', [pid, uid], (error, threadResults) => {
                    if (error) {
                        errorHandler(error, 'Something went wrong.', reject)
                        resolve({result: false, message: 'Something went wrong.'})
                    }
                    
                    if (threadResults.length >= 1) {
                        connection.query('DELETE FROM comment WHERE pid = ?', [pid])
                        connection.query('DELETE FROM post WHERE pid = ?', [pid], (error, threadResults) => {
                            if (error) {
                                errorHandler(error, 'Something went wrong.', reject)
                                resolve({result: false, message: 'Something went wrong.'})
                            }
                            resolve({result: true, message: 'Successfully delete thread.'})
                        })
                    } else {
                        resolve({result: false, message: 'Thread not found or No permission.'})
                    }
                })
            })
        },
        deleteComment: (uid, cid) => {
            return new Promise((resolve, reject) => {
                const { connection, errorHandler } = deps
                connection.query('SELECT * from comment WHERE cid = ? AND uid = ?', [cid, uid], (error, threadResults) => {
                    if (error) {
                        errorHandler(error, 'Something went wrong.', reject)
                        resolve({result: false, message: 'Something went wrong.'})
                    }
                    if (threadResults.length >= 1) {
                        connection.query('DELETE FROM comment WHERE cid = ?', [cid], (error, threadResults) => {
                            if (error) {
                                errorHandler(error, 'Something went wrong.', reject)
                                resolve({result: false, message: 'Something went wrong.'})
                            }
                            resolve({result: true, message: 'Successfully delete comment.'})
                        })
                    } else {
                        resolve({result: false, message: 'Comment not found or No permission.'})
                    }
                })
            })
        },
        search: (keywords, page) => {
            return new Promise((resolve, reject) => {
                const { connection, errorHandler } = deps
                connection.query('SELECT p.pid, p.uid, u.username, t.name AS tag, p.subject, p.ptime, p.uptime, p.rate, COUNT(c.pid) AS count, CEIL((COUNT(c.pid))/8) AS pages, MAX(c.ctime) AS lcom, GREATEST(IFNULL(MAX(c.ctime), 0), p.ptime) AS ntime, substr(substr(p.content, locate(?, p.content) + 5), 1, locate(?, substr(p.content, locate(?, p.content) + 5)) - 1) as cover FROM comment c RIGHT JOIN post p ON p.pid = c.pid INNER JOIN tags t ON p.tid = t.tid INNER JOIN users u ON u.uid = p.uid AND p.content LIKE ? OR p.subject LIKE ? GROUP BY p.pid ORDER BY ntime DESC LIMIT ?, ?', ['src="','"', 'src="', `%${keywords}%`, `%${keywords}%`, (parseInt(page)*8)-8, 8], (error, results) => {
                    if (error) {
                        errorHandler(error, 'Something went wrong.', reject)
                        resolve({result: false, message: 'Something went wrong.'})
                    }
                    if (results.length >= 1) {
                        if (parseInt(page) > 1) {
                            resolve({result: true, page: parseInt(page), search: results})
                        } else {
                            connection.query('SELECT COUNT(pid) AS sum FROM post WHERE content LIKE ? OR subject LIKE ? GROUP BY pid', [(parseInt(page)*8)-8, 8], (error, sumResults) => {
                                resolve({result: true, page: parseInt(page), sum: Math.ceil(parseInt(results.length)/8), search: results})
                            })
                        }
                    } else {
                        resolve({result: false, message: 'No others thread found.'})
                    }
                })
            })
        }
    }
}

module.exports = thread