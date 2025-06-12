from .crud import create_reservation, get_reservations_by_email
from .database import check_db_connection

from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from . import schemas, crud, models
from .database import SessionLocal, engine
from .models import Reservation, ReservationTable
from .schemas import ReservationCreate, ReservationResponse

app = FastAPI()

@app.get("/healthcheck")
async def healthcheck():
    db_status = check_db_connection()
    return {
        "api": "running",
        "database": db_status
    }

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.get("/restaurants/", response_model=list[schemas.Restaurant])
def read_restaurants(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    restaurants = crud.get_restaurants(db, skip=skip, limit=limit)
    return restaurants

@app.get("/restaurants/{restaurant_id}", response_model=schemas.Restaurant)
def read_restaurant(restaurant_id: int, db: Session = Depends(get_db)):
    db_restaurant = crud.get_restaurant(db, restaurant_id=restaurant_id)
    if db_restaurant is None:
        raise HTTPException(status_code=404, detail="Restaurant not found")
    return db_restaurant

@app.post("/reservations/", response_model=ReservationResponse)
def create_reservation_endpoint(
    reservation: ReservationCreate,
    db: Session = Depends(get_db)
):
    try:
        new_reservation = create_reservation(db, reservation)
        return {
            "reservation_id": new_reservation.reservation_id,
            "user_id": new_reservation.user_id,
            "status": "created"
        }
    except ValueError as e:
        raise HTTPException(status_code=404, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))


@app.get("/reservations/", response_model=list[ReservationResponse])
def read_reservations_by_email(
        email: str,
        db: Session = Depends(get_db)
):
    reservations = get_reservations_by_email(db, email)

    if reservations is None:
        raise HTTPException(
            status_code=404,
            detail="Пользователь с таким email не найден"
        )

    if not reservations:
        raise HTTPException(
            status_code=404,
            detail="У пользователя нет бронирований"
        )
    return reservations


@app.get("/users/is-admin/", response_model=schemas.IsAdminResponse)
def check_user_admin_status(
        email: str,
        db: Session = Depends(get_db)
):
    is_admin = crud.get_user_admin_status(db, email)

    if is_admin is None:
        raise HTTPException(
            status_code=404,
            detail="Пользователь с таким email не найден"
        )

    return {"isAdmin": is_admin}
# test 2