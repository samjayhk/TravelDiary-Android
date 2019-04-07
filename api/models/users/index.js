const bcrypt = require ('bcrypt')
const jwt = require ('jsonwebtoken')

let SALT_ROUNDS = 2
let SECRET = 'eutvh435874vj577yvy37b87'
let hash = text => bcrypt.hash(text, SALT_ROUNDS)

let createToken = ({
	uid
}) => jwt.sign({
	uid
}, SECRET, {
	expiresIn: '7d'
})

const checkId = (username, deps, result) => {
	const { connection, errorHandler } = deps
	connection.query('SELECT * FROM users WHERE username = ?', [username], (error, results) => {
		if (error) {
			errorHandler(error, 'User not found.', reject)
			result(false)
		}
		
		if (results.length >= 1) {
			result(results)
		} else {
			result(false)
		}
	})
}

const updateSignDate = (result, deps, itime) => {
	const { connection, errorHandler } = deps
	connection.query('SELECT * FROM usersinfo WHERE uid = ?', [result.uid], (error, results) => {
		if (results.length == 0) {
			connection.query('INSERT INTO usersinfo (uid, email, itime) VALUES (?, ?, ?)', [result.uid, result.email, Date.now()])
			itime(Date.now())
		} else {
			itime(results[0].itime)
			connection.query('UPDATE usersinfo SET itime = ? WHERE uid = ?', [Date.now(), result.uid])
		}
	})
}

const users = deps => {
	return {
		register: (username, password, email) => {
			return new Promise((resolve, reject) => {
				const { connection, errorHandler } = deps
				checkId(username, deps, async (result) => { 
					if (result) {
						resolve({ result: false, message: 'Username already exist.' })
					} else {
						const hashedPassword = await hash(password, SALT_ROUNDS)
						connection.query('INSERT INTO users (username, password, email) VALUES (?, ?, ?)', [username, hashedPassword, email], (error, results) => {
							if (error) {
								errorHandler(error, 'Failed to Register. Somethong went wrong.', reject)
								resolve({ result: false, message: 'Failed to Register. Somethong went wrong.' })
							}
								resolve({ result: true, message: 'Successfully register account.' })
						})
					}
				})
			})
		},
		login: (username, password) => {
			return new Promise((resolve, reject) => {
				const { connection, errorHandler } = deps
				checkId(username, deps, async (result) => {
					if (result) {
						const passwordIsValid = await bcrypt.compare(password, result[0].password)
						if (passwordIsValid) {
							updateSignDate(result[0], deps, async (itime) => {
								const token = await createToken(result[0])
								resolve({ result: true, message: 'Wellcome back. Your last login date is ' + (itime), username: result[0].username, reg: result[0].time, last: Date.now(), email: result[0].email, token: token})
							})
						} else {
							resolve({ result: false, message: 'Password incorrect.' })
						}
					} else {
						resolve({ result: false, message: 'Username not found.' })
					}
				})
			})
		},
		updatePassword: (uid, oldPassword, newPassword) => {
			return new Promise((resolve, reject) => {
				const { connection, errorHandler } = deps
				connection.query('SELECT * FROM users WHERE uid = ?', [uid], async (error, userResults) => {
					if (error) {
						errorHandler(error, 'Somethong went wrong.', reject)
						resolve({ result: false, message: 'Somethong went wrong.' })
					}
					if (userResults) {
						const passwordIsValid = await bcrypt.compare(oldPassword, userResults[0].password)
						if (passwordIsValid) {
							const hashedPassword = await hash(newPassword, SALT_ROUNDS)
							connection.query('UPDATE users SET password = ? WHERE uid = ?', [hashedPassword, uid], (error, updateResults) => {
								if (error) {
									errorHandler(error, 'Somethong went wrong.', reject)
									resolve({ result: false, message: 'Somethong went wrong.' })
								}
								resolve({result: true, message: 'Successfully update password.'})
							})
						} else {
							resolve({ result: false, message: 'Old password incorrect.' })
						}
					} else {
						resolve({result: false, message: 'Something went wrong.'})
					}
				})
					
			})
		}
	}
}


module.exports = users