from flask import Flask
import requests
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
        webhook_url = "https://discordapp.com/api/webhooks/1430328044152819773/9_sme81Acsj1IaWTkBN7B5GQU_agtShHCW5WtAU4y1MX4JvBmC7Jh4roPvb8oPJlFDBc"
        data = {"content": f"{username} has casted {spell}!"}
        response = requests.post(webhook_url, json=data)
        print(response.status_code)
        return "", 201
    else:
        return "", 204

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)  # Use 0.0.0.0 to accept external connections
