import base64
import hashlib
import os

from uuid import uuid4  # Für eindeutige State-Generierung

import requests
from requests_oauthlib import OAuth2Session

# Konfiguration
CLIENT_ID = "8a-nu"  # Ersetze mit deinem Client-ID
AUTHORIZATION_BASE_URL = "https://vlatka.vertical-life.info/auth/realms/Vertical-Life"
TOKEN_URL = "https://vlatka.vertical-life.info/auth/realms/Vertical-Life/protocol/openid-connect/token"
REDIRECT_URI = "https://www.8a.nu/callback"  # Muss zur App-Registrierung passen
SCOPES = ["openid", "email", "profile"]

# Beispiel-API-Endpunkt für geschützte Daten
API_ENDPOINT = "https://api.vertical-life.info"  # Ersetze mit deinem API-Endpoint


def generate_code_verifier_and_challenge():
    """
    Generiert den Code Verifier und Code Challenge für PKCE.
    """
    # Code Verifier
    code_verifier = base64.urlsafe_b64encode(os.urandom(32)).rstrip(b"=").decode("utf-8")
    # Code Challenge
    code_challenge = base64.urlsafe_b64encode(
        hashlib.sha256(code_verifier.encode("utf-8")).digest()
    ).rstrip(b"=").decode("utf-8")
    return code_verifier, code_challenge


def oauth_login():
    """
    OAuth2-Login mit PKCE. Führt den Benutzer durch den Authentifizierungsprozess
    und gibt einen Access-Token zurück.
    """
    # PKCE-Verifier und Challenge generieren
    code_verifier, code_challenge = generate_code_verifier_and_challenge()

    # Eindeutigen State generieren
    state = str(uuid4())  # Zufällige UUID als State

    # OAuth2Session konfigurieren
    oauth = OAuth2Session(
        CLIENT_ID,
        redirect_uri=REDIRECT_URI,
        scope=SCOPES,
        state=state  # State explizit setzen
    )

    # Authorization-URL erstellen
    authorization_url, generated_state = oauth.authorization_url(
        AUTHORIZATION_BASE_URL,
        code_challenge=code_challenge,
        code_challenge_method="S256",
    )

    print(f"Bitte öffne diese URL in deinem Browser, um dich anzumelden:\n{authorization_url}")
    print(f"Generierter State: {generated_state}")

    # Benutzer gibt die Redirect-URL mit dem Code ein
    redirect_response = f"https://www.8a.nu/callback?code={code_challenge}&state={state}"

    # Debugging: Überprüfen, ob der State in der Redirect-URL vorhanden ist
    if f"state={state}" not in redirect_response:
        raise ValueError("Der State-Parameter in der Redirect-URL stimmt nicht mit dem generierten überein.")

    # Access-Token mit dem Authorization-Code und dem PKCE-Verifier anfordern
    token = oauth.fetch_token(
        TOKEN_URL,
        authorization_response=redirect_response,
        client_id=CLIENT_ID,
        code_verifier=code_verifier,  # Wichtig für PKCE
        state=state
    )

    print(f"Access Token erhalten: {token}")
    return token


def fetch_protected_data(access_token):
    """
    Ruft geschützte Daten von der API ab, indem der Access-Token verwendet wird.
    """
    headers = {
        "Authorization": f"Bearer {access_token}"
    }
    response = requests.get(API_ENDPOINT, headers=headers)

    if response.status_code == 200:
        print(f"Daten erfolgreich abgerufen:\n{response.json()}")
    else:
        print(f"Fehler beim Abrufen der Daten: {response.status_code}, {response.text}")


if __name__ == "__main__":
    # Schritt 1: Login und Access-Token abrufen
    token = oauth_login()

    # Schritt 2: Daten mit Access-Token abrufen
    fetch_protected_data(token["access_token"])
