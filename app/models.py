from sqlalchemy import Column, Integer, String, Text, Numeric, Time, DateTime, func
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