from sqlalchemy import Column, Integer, String, Text, Numeric, Time, DateTime, func, ForeignKey, Date, TIMESTAMP, \
    Boolean
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()

class Restaurant(Base):
    __tablename__ = 'restaurants'

    restaurant_id = Column(Integer, primary_key=True, index=True)
    restaurant_name = Column(String(100), nullable=False)
    restaurant_address = Column(String(200), nullable=False)
    restaurant_phone = Column(String(20), nullable=False)
    restaurant_description = Column(Text)
    restaurant_logo = Column(Text)
    restaurant_rating = Column(Numeric(3, 2), default=0.0)
    open_time = Column(Time, nullable=False)
    close_time = Column(Time, nullable=False)
    created_at = Column(DateTime(timezone=True), server_default=func.now())

    def __repr__(self):
        return f"<Restaurant(id={self.restaurant_id}, name='{self.restaurant_name}')>"

class User(Base):
    __tablename__ = "users"

    user_id = Column(Integer, primary_key=True, index=True)
    user_name = Column(String(100), nullable=False)
    user_phone = Column(String(20))
    user_email = Column(String(255), nullable=False, unique=True)
    date_of_created = Column(DateTime(timezone=True), server_default=func.now())
    isadmin = Column(Boolean, default=False)
    email_confirmed = Column(Boolean, default=False)

class Reservation(Base):
    __tablename__ = "reservations"

    reservation_id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.user_id"), nullable=False)
    restaurant_id = Column(Integer, ForeignKey("restaurants.restaurant_id"), nullable=False)
    reservation_date = Column(Date, nullable=False)
    reservation_time = Column(TIMESTAMP, nullable=False)  # Храним дату+время
    guests = Column(Integer, nullable=False)
    status = Column(String(20), default="pending")  # pending/confirmed/cancelled
    created_at = Column(TIMESTAMP(timezone=True), server_default=func.now())

class ReservationTable(Base):
    __tablename__ = "reservation_tables"

    reservation_id = Column(Integer, ForeignKey("reservations.reservation_id"), primary_key=True)
    table_id = Column(Integer, ForeignKey("tables.table_id"), primary_key=True)

