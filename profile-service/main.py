from fastapi import FastAPI

app = FastAPI()

@app.get("/profile/health")
def health_check():
    return {"status": "ok"}
