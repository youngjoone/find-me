from fastapi import FastAPI, Request, Response
from loguru import logger
import uuid

app = FastAPI()

@app.middleware("http")
async def add_request_id_header(request: Request, call_next):
    request_id = request.headers.get("X-Request-ID")
    if not request_id:
        request_id = str(uuid.uuid4())
    
    with logger.contextualize(request_id=request_id):
        response = await call_next(request)
        response.headers["X-Request-ID"] = request_id
        return response

# Configure loguru to include request_id
logger.add("file.log", format="{time} {level} {message} {extra[request_id]}")

@app.get("/profile/health")
def health_check():
    logger.info("Health check requested")
    return {"status": "ok"}
