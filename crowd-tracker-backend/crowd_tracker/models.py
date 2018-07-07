from pymodm import MongoModel, fields
from commons.utils.default_model_manager import DefaultManager

class Station(MongoModel):
	code = fields.CharField(max_length=50)
	name = fields.CharField(max_length=100)
	lat = fields.Decimal128Field()
	lng = fields.Decimal128Field()

	objects = DefaultManager()

	class Meta:
		collection_name = 'Station'
		final = True


class CTRickshaw(MongoModel):
	name = fields.CharField(max_length=100)
	rickshawNumber = fields.CharField(max_length=100)
	lat = fields.Decimal128Field()
	lng = fields.Decimal128Field()

	objects = DefaultManager()

	class Meta:
		collection_name = 'CTRickshaw'
		final = True