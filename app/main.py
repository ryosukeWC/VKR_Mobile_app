from fastapi import FastAPI
from .database import check_db_connection

app = FastAPI()

@app.get("/healthcheck")
async def healthcheck():
    db_status = check_db_connection()
    return {
        "api": "running",
        "database": db_status
    }