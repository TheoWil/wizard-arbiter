from flask import Flask
from flask_cors import CORS  # Install with: pip install flask-cors

app = Flask(__name__)
CORS(app)  # This will enable CORS for all routes

@app.route('/<username>/<spell>', methods=['GET', 'POST', 'OPTIONS'])
def handle_request(username=None, spell=None):
    # Log everything about the request
    print("\n=== INCOMING REQUEST ===")
    print(f"Username: {username}")
    print(f"Spell: {spell}")
    print("=======================\n")
    
    if username and spell:
        return "", 201
    else:
        return "", 204

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)  # Use 0.0.0.0 to accept external connections
