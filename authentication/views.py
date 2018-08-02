from django.shortcuts import render
from django.http import HttpResponse, HttpResponseRedirect, Http404
from django.shortcuts import render, get_object_or_404, redirect
from django.shortcuts import render, render_to_response
from .models import *
from .forms import *
from passlib.hash import pbkdf2_sha256
from django.http import JsonResponse
from django.core.serializers import json
from django.views.decorators.csrf import csrf_exempt
from django.template import RequestContext
from django.conf import settings
import json
import time
import datetime
from django.db import transaction
from django.utils.crypto import get_random_string
from django.conf import settings
from django.core.mail import send_mail
from fcm.utils import FCMMessage
from django.views.decorators.cache import cache_control
from django.views.decorators.cache import cache_page

@csrf_exempt
def index(request):
	return render_to_response('index.html')
@csrf_exempt
def index_student(request):
	sessionid = request.session['sessionid']
	return render(request, 'student.html', {'sessionid': sessionid})
@csrf_exempt
def index_proctor(request):
	sessionid=request.session['sessionid']
	return render(request,'student.html',{'sessionid':sessionid})
@csrf_exempt
def index_faculty(request):
	sessionid = request.session['sessionid']
	return render(request, 'student.html', {'sessionid': sessionid})

@csrf_exempt
def logout(request):
	return render_to_response('index.html')

@csrf_exempt
def login_proctor(request):
	if  request.method=="POST":
		#Login_Form = LoginForm(request.POST)
		#if Login_Form.is_valid():
		email=json.loads(request.body.decode("utf-8"))['email']
		password=json.loads(request.body.decode("utf-8"))['password']
		#hash_password = pbkdf2_sha256.encrypt(password,rounds=500000, salt_size=32)
		#print("The hashed password is"+str(hash_password))
		
		try:
			#print("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
			#hash=pbkdf2_sha256.using(rounds=8000, salt_size=10).hash(Login_Form.cleaned_data["password"])
			
			authority_object=Authority.objects.get(
				aemail=email,
				atype='Proctore'
				)


			#print("errorerrrrrrfrom django.shortcuts import render, render_to_responserrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr")
			#user_login=pbkdf2_sha256.using(rounds=200000, salt_size=32).verify(Login_Form.cleaned_data["password"], authority_object.password)
		except Exception as e:
			#messages.info(request, 'Login Failed')
			
			print("Login Failed "+str(e))
			
		dbpassword=authority_object.apassword

		print("The database password is "+str(dbpassword))

		if pbkdf2_sha256.verify(password,dbpassword):
			sessionuserid = authority_object.aid
			request.session["sessionid"]=authority_object.aid

			#return HttpResponseRedirect("/index/student/")
			#return render_to_response('student.html')
			#return redirect("/index/student/")
			return JsonResponse({"url":"/proctor/","sessionid":sessionuserid,"return_status":1},safe=False)
				
		else:
			return JsonResponse({"sessionid":-1, "return_status": 2}, safe=False)
				

		
@csrf_exempt
def login_faculty(request):
	if  request.method=="POST":
		#Login_Form = LoginForm(request.POST)
		#if Login_Form.is_valid():
		email=json.loads(request.body.decode("utf-8"))['email']
		password=json.loads(request.body.decode("utf-8"))['password']
		#hash_password = pbkdf2_sha256.encrypt(password,rounds=300000, salt_size=32)
		try:
			#print("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
			#hash=pbkdf2_sha256.using(rounds=8000, salt_size=10).hash(Login_Form.cleaned_data["password"])
			
			authority_object=Authority.objects.get(
				aemail=email,
				atype='faculty',
				)
			#print("errorerrrrrrfrom django.shortcuts import render, render_to_responserrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr")
			#user_login=pbkdf2_sha256.using(rounds=200000, salt_size=32).verify(Login_Form.cleaned_data["password"], authority_object.password)
		except Exception as e:
			#messages.info(request, 'Login Failed')
			print("Login Failed"+str(e))

		dbpassword=authority_object.apassword

		if pbkdf2_sha256.verify(password,dbpassword):
			request.session["sessionid"]=authority_object.aid
			sessionuserid=authority_object.aid
			return JsonResponse({"url": "/proctor/", "sessionid": sessionuserid, "return_status": 1}, safe=False)

		else:
			return JsonResponse({"sessionid": -1, "return_status": 2}, safe=False)
			#return JsonResponse({'url':"/faculty/"},safe=False)
				

		
@csrf_exempt
def login_student_member(request):
	if  request.method=="POST":
		#Login_Form = LoginForm(request.POST)
		#if Login_Form.is_valid():
		email=json.loads(request.body.decode("utf-8"))['email']
		password=json.loads(request.body.decode("utf-8"))['password']
		#hash_password = pbkdf2_sha256.encrypt(password,rounds=500000, salt_size=32)
			
		try:
			#print("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
			#hash=pbkdf2_sha256.using(rounds=8000, salt_size=10).hash(Login_Form.cleaned_data["password"])
			authority_object=Authority.objects.get(
				aemail=email,
				
				atype='student_member',
				)
			
		except Exception as e:
			#messages.info(request, 'Login Failed')
			print("Login Failed"+str(e))

		dbpassword=authority_object.apassword
		if pbkdf2_sha256.verify(password,dbpassword):
			request.session["sessionid"]=authority_object.aid
			return JsonResponse({'url':"/studentmember/"},safe=False)
				








@csrf_exempt
def add_proctor(request):
	if request.method=="POST":
		
		#if not request.session.get('sessionid',None):
		#	return HttpResponse("Login required")
		
		#sessionid=request.session['sessionid']
		sessionid = json.loads(request.body.decode('utf-8'))["sessionid"]
		print("The session id is "+str(sessionid))
		name=json.loads(request.body.decode('utf-8'))["name"]
		random_string = get_random_string(length=32)
		#password=json.loads(request.body.decode('utf-8'))["password"]

		email=json.loads(request.body.decode('utf-8'))["email"]
		lemail=[]
		lemail.append(email)
		send_mail('No Rag', 
			'This is your password - '+random_string+' Please Change after you login',
			settings.EMAIL_HOST_USER,
			lemail, 
			fail_silently=False
			)
		mobile_no=json.loads(request.body.decode('utf-8'))["phone"]
		designation=json.loads(request.body.decode('utf-8'))["designation"]
		branch=json.loads(request.body.decode('utf-8'))['branch']
		hash_password = pbkdf2_sha256.encrypt(random_string,rounds=500000, salt_size=32)
		print ("THE SESSION ID IS"+str(sessionid))        
		try:
			entry_proctor=Authority(aname=name,
				amobile_no=mobile_no,
				apassword=hash_password,
				adesignation=designation,
				aemail=email,
				atype='Proctore',
				abranch=branch
				)
		except Exception as e:
			print ("The error is"+str(e))
		entry_proctor.save()
		return JsonResponse({'message':"Proctore added successfully"},safe=False)
        

@csrf_exempt
def add_faculty(request):
	if request.method=='POST':
		#if not request.session.get('sessionid',None):
		#	return HttpResponse("Login required")
		#sessionid=request.session['sessionid']
		sessionid = json.loads(request.body.decode('utf-8'))["sessionid"]
		name=json.loads(request.body.decode('utf-8'))["name"]
		random_string = get_random_string(length=32)
		

		email=json.loads(request.body.decode('utf-8'))["email"]
		lemail=[]
		lemail.append(email)
		send_mail('No Rag', 
			'This is your password - '+random_string+' Please Change after you login',
			settings.EMAIL_HOST_USER,
			lemail, 
			fail_silently=False
			)
		branch=json.loads(request.body.decode('utf-8'))['branch']
		mobile_no=json.loads(request.body.decode('utf-8'))["phone"]
		designation=json.loads(request.body.decode('utf-8'))["designation"]
		hash_password = pbkdf2_sha256.encrypt(random_string,rounds=300000, salt_size=32)
		print ("THE SESSION ID IS"+str(sessionid))
		try:
			entry_faculty=Authority(aname=name,amobile_no=mobile_no,apassword=hash_password,adesignation=designation,aemail=email,atype='faculty',abranch=branch)
		except Exception as e:
			print ("The error is"+str(e))
		
		entry_faculty.save()
		return JsonResponse({'message': "Faculty added successfully"}, safe=False)
			
			


@csrf_exempt
def add_student_member(request):
	if request.method=="POST":
		#if not request.session.get('sessionid',None):
		#	return HttpResponse("Login required")
		#sessionid=request.session['sessionid']
		sessionid = json.loads(request.body.decode('utf-8'))["sessionid"]
		name=json.loads(request.body.decode('utf-8'))["name"]
		random_string = get_random_string(length=32)
		
		email=json.loads(request.body.decode('utf-8'))["email"]
		lemail=[]
		lemail.append(email)
		send_mail('No Rag', 
			'This is your password - '+random_string+' Please Change after you login',
			settings.EMAIL_HOST_USER,
			lemail, 
			fail_silently=False
			)
		mobile_no=json.loads(request.body.decode('utf-8'))["phone"]
		branch=json.loads(request.body.decode('utf-8'))['branch']
		#designation=json.loads(request.body.decode('utf-8'))["designation"]
		designation="No Designation"
		hash_password = pbkdf2_sha256.encrypt(random_string,rounds=200000, salt_size=32)
		print ("THE SESSION ID IS"+str(sessionid))
		try:
			entry_student_member=Authority(aname=name,amobile_no=mobile_no,apassword=hash_password,adesignation=designation,aemail=email,atype='student_member',abranch=branch)
		except Exception as e:
			print ("The error is"+str(e))
		entry_student_member.save()
		return JsonResponse({'message': "Student Member added successfully"}, safe=False)

@csrf_exempt
def change_password(request):
	if request.method=="POST":
		#if not request.session.get('sessionid',None):
		#	return HttpResponse("Login required")
		#sessionid=request.session['sessionid']
		sessionid = json.loads(request.body.decode('utf-8'))["sessionid"]
		oldpassword=json.loads(request.body.decode("utf-8"))['oldpassword']
		newpassword=json.loads(request.body.decode("utf-8"))['newpassword']
		cnewpassword=json.loads(request.body.decode("utf-8"))['cnewpassword']
		print("The old password is "+str(oldpassword))
		print("The new password is "+str(newpassword))
		print("The cnew password is "+str(cnewpassword))
		try:
			authority_object=Authority.objects.get(aid=sessionid)
			dbpassword=authority_object.apassword
			atype=authority_object.atype

		except Exception as e:
			print("The exception is"+str(e))

		if newpassword==cnewpassword and pbkdf2_sha256.verify(oldpassword,dbpassword):
			
			if atype=='Proctore':
				hash_password=pbkdf2_sha256.encrypt(newpassword,rounds=500000, salt_size=32)
			elif atype=='faculty':
				hash_password=pbkdf2_sha256.encrypt(newpassword,rounds=300000, salt_size=32)
			elif atype=='student_member':
				hash_password=pbkdf2_sha256.encrypt(newpassword,rounds=200000, salt_size=32)

			authority_object.apassword=hash_password
			authority_object.save()
			return JsonResponse({'message': "Password Changed Successfully",'return_status':1}, safe=False)

		else:
			return JsonResponse({'message':"Password Didnt Match",'return_status':2},safe=False)
			

@csrf_exempt
def forgot_password(request):
	if request.method=="POST":
		email=json.loads(request.body.decode('utf-8'))['email']
		try:
			authority_object=Authority.objects.get(aemail=email)
		except Exception as e:
			print("The exception is "+str(e))
			authority_object=None


		if authority_object is not None:

			random_string = get_random_string(length=32)
		
			lemail=[]
			lemail.append(email)
			send_mail(
				'No Rag',
				'This is your password - '+random_string+' Please Change after you login',
				settings.EMAIL_HOST_USER,
				lemail,
				fail_silently=False
				)

			authority_type=authority_object.atype
			
			if authority_type=='Proctore':
				hash_password = pbkdf2_sha256.encrypt(random_string,rounds=500000, salt_size=32)
			elif authority_type=='faculty':
				hash_password=pbkdf2_sha256.encrypt(random_string,rounds=300000, salt_size=32)
			elif authority_type=='student_member':
				hash_password = pbkdf2_sha256.encrypt(random_string,rounds=200000, salt_size=32)

		authority_object.apassword=hash_password
		authority_object.save()

		return HttpResponse()

@csrf_exempt
def change_settings(request):
	if request.method=='POST':
		#if not request.session.get('sessionid',None):
		#	return HttpResponse("Login required")
		#sessionid=request.session['sessionid']
		sessionid = json.loads(request.body.decode('utf-8'))["sessionid"]
		name=json.loads(request.body.decode('utf-8'))['name']
		email=json.loads(request.body.decode('utf-8'))['email']
		phone=json.loads(request.body.decode('utf-8'))['phone']
		branch=json.loads(request.body.decode('utf-8'))['branch']
		designation=json.loads(request.body.decode('utf-8'))['designation']

		try:
			authority_object=Authority.objects.get(aid=sessionid)
		except Exception as e:
			print("The exception is "+str(e))

		if name is not None:
			authority_object.aname=name
		if email is not None:
			authority_object.aemail=email
		if phone is not None:
			authority_object.amobile_no=phone
		if branch is not None:
			authority_object.abranch=branch
		if designation is not None:
			authority_object.adesignation=designation

		authority_object.save()
		try:
			new_authority_object=Authority.objects.get(aid=sessionid)
		except Exception as e:
			print("The exception is "+str(e))

		newemail=new_authority_object.aemail
		newname=new_authority_object.aname
		newphone=new_authority_object.amobile_no
		newbranch=new_authority_object.abranch
		newdesignation=new_authority_object.adesignation

		context_authority={}
		context_authority['email']=newemail
		context_authority['name']=newname
		context_authority['phone']=newphone
		context_authority['branch']=newbranch
		context_authority['designation']=newdesignation

		print("The context authority is"+str(context_authority))

		return JsonResponse(context_authority,safe=False)





#def change_about(request):


@csrf_exempt
def particular_authority(request):
	if request.method=='POST':
		#if not request.session.get('sessionid',None):
		#	return HttpResponse("Login required")
		#sessionid=request.session['sessionid']
		sessionid=json.loads(request.body.decode('utf-8'))['sessionid']
		atype=json.loads(request.body.decode('utf-8'))['atype']
		if atype == 'all':

			try:
				authority_object=Authority.objects.all()
			except Exception as e:
				print("The exception is"+str(e))

		else:
			try:
				authority_object=Authority.objects.filter(atype=atype)
			except Exception as e:
				print("The exception is"+str(e))
		
		
		context_authority_list=[]
		for authority in authority_object:
			context_authority={}
			context_authority['name']=authority.aname
			context_authority['phone']=authority.amobile_no
			context_authority['email']=authority.aemail
			context_authority['branch']=authority.abranch
			context_authority['designation']=authority.adesignation
			context_authority['atype']=authority.atype
			#print("This is context "+str(context_authority))
			context_authority_list.append(context_authority)

		#print("The context authority list is"+str(context_authority_list))
		response=JsonResponse(context_authority_list,safe=False)
		MAX_AGE = getattr(settings, 'CACHE_CONTROL_MAX_AGE', 2592000)
		response['Cache-Control'] = 'max-age=%d' % MAX_AGE
		return response
		#return JsonResponse(context_authority_list,safe=False)

@csrf_exempt
def show_details(request):
	if request.method=='POST':
		#if not request.session.get('sessionid',None):
		#	return HttpResponse("Login required")
		#sessionid=request.session['sessionid']
		sessionid=json.loads(request.body.decode('utf-8'))['sessionid']

		#atype=json.loads(request.body.decode('utf-8'))['atype']
		try:
			authority_object=Authority.objects.get(aid=sessionid)
		except Exception as e:
			print("The exception is"+str(e))
		
		context_authority={}
		
		if authority_object:
			context_authority['name']=authority_object.aname
			context_authority['phone']=authority_object.amobile_no
			context_authority['email']=authority_object.aemail
			context_authority['branch']=authority_object.abranch
			context_authority['designation']=authority_object.adesignation
			print("This is context "+str(context_authority))
			
		#return JsonResponse(context_authority,safe=False)
		response=JsonResponse(context_authority,safe=False)
		MAX_AGE = getattr(settings, 'CACHE_CONTROL_MAX_AGE', 2592000)
		response['Cache-Control'] = 'max-age=%d' % MAX_AGE
		return response


@csrf_exempt
def student_registration(request):
	if request.method == "POST":

		roll_no = json.loads(request.body.decode("utf-8"))['roll_no']
		email = json.loads(request.body.decode("utf-8"))['email']
		password=json.loads(request.body.decode("utf-8"))['password']
		cpassword=json.loads(request.body.decode("utf-8"))['cpassword']
		#random_string = get_random_string(length=32)
		if password==cpassword:
			newpassword = pbkdf2_sha256.encrypt(
			password,
			rounds=100000,
			salt_size=32,
		)

		try:
			student_object = Students(
				roll_no=roll_no,
				email=email,
				password=newpassword,
			)
			student_object.save()
		except Exception as e:
			print("The excepion is " + str(e))

		try:
			student_obj=Students.objects.get(email=email,roll_no=roll_no)
			sid=student_obj.sid
		except Exception as  e:
			print("getting the object "+str(e))
		try:
			student_detail_object=Student_details(sid=Students(sid))
			student_detail_object.save()
		except Exception as e:
			print("In student registration"+str(e))


		return JsonResponse({'response_data': "1"}, safe=False)

	else:
		return JsonResponse({'response_data': "2"}, safe=False)

@csrf_exempt
def student_login(request):
	if request.method == "POST":
		roll_no = json.loads(request.body.decode("utf-8"))['roll_no']
		password = json.loads(request.body.decode("utf-8"))['password']
		#hash_password = pbkdf2_sha256.encrypt(password, rounds=100000, salt_size=32)

		try:
			student_object = Students.objects.get(roll_no=roll_no)
			sessionuserid = student_object.sid
			dbpassword = student_object.password

			if pbkdf2_sha256.verify(password, dbpassword):
				#request.session["sessionid"] = authority_object.aid
				request.session["sessionid"] = sessionuserid
				return JsonResponse({'return_status': 1, 'sessionid': sessionuserid}, safe=False)
		except Exception as e:
			print("The exception is" + str(e))

		return JsonResponse({'return_status': 2, 'sessionid': "-1"}, safe=False)

@csrf_exempt
def show_student_detail(request):
	if request.method=='POST':
		#if not request.session.get('sessionid',None):
		#	return HttpResponse("Login required")
		#sessionid=request.session['sessionid']
		sessionid=json.loads(request.body.decode("utf-8"))["sessionid"]
		try:
			student_detail_object=Student_details.objects.get(sid=sessionid)
			student_object=Students.objects.get(sid=sessionid)
		except Exception as e:
			print("Show_student_detail"+str(e))
		email=student_object.email
		roll_no=student_object.roll_no
		name=student_detail_object.name
		address=student_detail_object.address
		mobile_no=student_detail_object.mobile_no
		g_mobile_no=student_detail_object.g_mobile_no

		context_student_detail={}
		context_student_detail['email']=email
		context_student_detail['roll_no']=roll_no
		context_student_detail['name']=name
		context_student_detail['address']=address
		context_student_detail['mobile_no']=mobile_no
		context_student_detail['g_mobile_no']=g_mobile_no
		#return JsonResponse(context_student_detail,safe=False)
		response=JsonResponse(context_student_detail,safe=False)
		MAX_AGE = getattr(settings, 'CACHE_CONTROL_MAX_AGE', 2592000)
		response['Cache-Control'] = 'max-age=%d' % MAX_AGE
		return response
@csrf_exempt
def update_student_detail(request):
	if request.method == 'POST':
		#if not request.session.get('sessionid', None):
		#	return HttpResponse("Login required")
		#sessionid = request.session['sessionid']
		sessionid=json.loads(request.body.decode('utf-8'))['sessionid']
		name=json.loads(request.body.decode('utf-8'))['name']
		email=json.loads(request.body.decode('utf-8'))['email']
		address=json.loads(request.body.decode('utf-8'))['address']
		roll_no=json.loads(request.body.decode('utf-8'))['roll_no']
		mobile_no=json.loads(request.body.decode('utf-8'))['mobile_no']
		g_mobile_no=json.loads(request.body.decode('utf-8'))['g_mobile_no']
		if roll_no is not None or email is not None:
			try:
				student_object=Students.objects.get(sid=sessionid)
			except Exception as e:
				print("In the update student "+str(e))
			if roll_no is not None:
				student_object.roll_no=roll_no
			if email is not None:
				student_object.email=email
		if name is not None or address is not None or mobile_no is not None or g_mobile_no is not None:
			try:
				student_detail_object=Student_details.objects.get(sid=sessionid)
			except Exception as e:
				print("In the update student detail"+str(e))
			if name is not None:
				student_detail_object.name=name
			if address is not None:
				student_detail_object.address=address
			if mobile_no is not None:
				student_detail_object.mobile_no=mobile_no
			if g_mobile_no is not None:
				student_detail_object.g_mobile_no=g_mobile_no

		student_object.save()
		student_detail_object.save()

		context_student_update_detail={}
		new_student_object=Students.objects.get(sid=sessionid)
		new_student_detail_object=Student_details.objects.get(sid=sessionid)
		context_student_update_detail['email']=new_student_object.email
		context_student_update_detail['roll_no']=new_student_object.roll_no
		context_student_update_detail['name']=new_student_detail_object.name
		context_student_update_detail['address']=new_student_detail_object.address
		context_student_update_detail['mobile_no']=new_student_detail_object.mobile_no
		context_student_update_detail['g_mobile_no']=new_student_detail_object.g_mobile_no

		return JsonResponse(context_student_update_detail,safe=False)

@csrf_exempt
def student_change_password(request):
	if request.method == 'POST':
		#if not request.session.get('sessionid', None):
		#	return HttpResponse("Login required")
		#sessionid = request.session['sessionid']
		sessionid=json.loads(request.body.decode('utf-8'))['sessionid']
		oldpassword=json.loads(request.body.decode('utf-8'))['oldpassword']
		newpassword=json.loads(request.body.decode('utf-8'))['newpassword']
		cnewpassword=json.loads(request.body.decode('utf-8'))['cnewpassword']
		try:
			student_object = Students.objects.get(sid=sessionid)
			dbpassword = student_object.password


		except Exception as e:
			print("The exception is" + str(e))

		if newpassword == cnewpassword and pbkdf2_sha256.verify(oldpassword, dbpassword):
			hash_password = pbkdf2_sha256.encrypt(newpassword, rounds=100000, salt_size=32)
			student_object.password = hash_password
			student_object.save()
			return JsonResponse({'message': "Password Changed Successfully",'return_status':1}, safe=False)

		else:
			return JsonResponse({'message': "Password Didnt Match",'return_status':2}, safe=False)




@csrf_exempt
def student_forgot_password(request):
	if request.method == 'POST':
		email = json.loads(request.body.decode('utf-8'))['email']
		try:
			student_object = Students.objects.get(email=email)
		except Exception as e:
			print("The exception is " + str(e))

		if student_object:
			random_string = get_random_string(length=32)

			lemail = []
			lemail.append(email)
			send_mail(
				'No Rag',
				'This is your password - ' + random_string + ' Please Change after you login',
				settings.EMAIL_HOST_USER,
				lemail,
				fail_silently=False
			)

		hash_password = pbkdf2_sha256.encrypt(random_string, rounds=100000, salt_size=32)

		student_object.password = hash_password
		student_object.save()

		return HttpResponse()

@csrf_exempt
def add_complain(request):
	if request.method == 'POST':

		sessionid=json.loads(request.body.decode('utf-8'))['sessionid'];
		text=json.loads(request.body.decode('utf-8'))['text']
		attachment=json.loads(request.body.decode('utf-8'))['attachment']
		latitude=json.loads(request.body.decode('utf-8'))['latitude']
		longitude=json.loads(request.body.decode('utf-8'))['longitude']
		current_date=datetime.datetime.now().strftime('%H:%M:%S')
		status="not processed"
		#notification = json.loads(request.body.decode('utf-8'))['notification']
		#current_date = time.time()
		notification_student="Complain has been sent successfully"
		notification_authority="A new complain has been registered"
		#For entering Student Notification

		try:
			student_notification_object = Notification_Students(sid=Students(sessionid), notification=notification_student,
																date=current_date)
		except Exception as e:
			print("Add notification" + str(e))


		with transaction.atomic():

			#For entering Notification to authorities in bulk amount
			FCMMessage().send({'message': text}, to='/topics/authority')
			student_notification_object.save()
			authority_objects=Authority.objects.all()
			authority_object_list=[]
			for authority in authority_objects:
				aid=authority.aid
				particular_authority_object=Notification_Authority(aid=Authority(aid),notification=notification_authority,date=current_date)
				authority_object_list.append(particular_authority_object)

			Notification_Authority.objects.bulk_create(authority_object_list)

		try:
			complain_object=Complain(sid=Students(sessionid),complain_txt=text,attachment=attachment,date=current_date,status=status,latitude=latitude,longitude=longitude)
		except Exception as e:
			print("In add complain "+str(e))
		complain_object.save()

		return JsonResponse({'message': "Complain Added Successfully",'return_status':1}, safe=False)

@csrf_exempt
def update_complain_status(request):
	if request.method == "POST":
		#if not request.session.get('sessionid', None):
		#	return HttpResponse("Login required")
		#sessionid = request.session['sessionid']
		#sessionid=json.loads(request.body.decode('utf-8'))['sessionid']
		newstatus=json.loads(request.body.decode('utf-8'))['newstatus']
		cid=json.loads(request.body.decode('utf-8'))['cid']
		try:
			old_complain_object=Complain.objects.get(cid=int(cid))
		except Exception as e:
			print("In the update complain status "+str(e))
		notification_student = "New Action has been taken against your complain"
		notification_authority = "A new complain has been registered"
		# For entering Student Notification
		studentid=old_complain_object.sid
		#current_date=time().time()
		current_date = datetime.datetime.now().strftime('%H:%M:%S')

		FCMMessage().send({'message':notification_student}, to='/topics/student')

		try:
			student_notification_object = Notification_Students(sid=studentid, notification=notification_student,
																date=current_date)
		except Exception as e:
			print("Add notification" + str(e))


		student_notification_object.save()
		old_complain_object.status=newstatus
		old_complain_object.save()
		context_complain_status_object={}
		context_complain_status_object['status']=newstatus
		context_complain_status_object['cid']=cid

		return JsonResponse(context_complain_status_object,safe=False)

@csrf_exempt
def see_complains(request):
	if request.method == "POST":
		#if not request.session.get('sessionid', None):
		#	return HttpResponse("Login required")
		#sessionid = request.session['sessionid']
		sessionid=json.loads(request.body.decode('utf-8'))['sessionid']

		try:
			total_complain_objects=Complain.objects.all()
		except Exception as e:
			print("In the see complain object"+str(e))
		complain_object_context_list=[]
		totalcount=0
		if total_complain_objects:
			for complain_object in total_complain_objects[::-1]:
				complain_object_context={}


				try:
					complain_status_object = Complain_Status.objects.filter(cid=complain_object.cid)
					totalvotes=len(complain_status_object)
				except Exception as e:
					print("No total vote present")
					totalvotes=0


				try:
					my_complain_status_object=Complain_Status.objects.get(cid=complain_object.cid,aid=sessionid)
					myvoting=my_complain_status_object.voting
					#newmyvoting

				except Exception as e:
					print("My complain status is not present "+str(e))
					myvoting="0"
				complain_owner_object=Student_details.objects.get(sid=complain_object.sid)

				complain_object_context['cid'] = complain_object.cid
				complain_object_context['sid'] = str(Students(complain_object.sid))
				complain_object_context['severity_of_punishment']=calculate_punishment(int(complain_object.cid))
				#complain_object_context['severity_of_punishment']=""
				complain_object_context['student_name'] = complain_owner_object.name
				complain_object_context['mobile_no'] = complain_owner_object.mobile_no
				complain_object_context['g_mobile_no'] = complain_owner_object.g_mobile_no
				complain_object_context['complain_txt'] = complain_object.complain_txt
				complain_object_context['attachment'] = complain_object.attachment
				complain_object_context['latitude']=str(complain_object.latitude)
				complain_object_context['longitude']=str(complain_object.longitude)
				complain_object_context['date'] = complain_object.date.strftime("%d-%m-%Y %I:%M %p")
				complain_object_context['status'] = complain_object.status
				complain_object_context['totalvotes']=totalvotes
				#if type(myvoting) is not int:
				#	complain_object_context['myvote']=int(myvoting)
				#else:
				complain_object_context['myvote'] = str(myvoting)
				print (complain_object_context)
				complain_object_context_list.append(complain_object_context)
			#complain_object_context_list.append({'return_status':1})

		#else:
		#	complain_object_context_list.append({'return_status':2})
		response=JsonResponse(complain_object_context_list,safe=False)
		MAX_AGE = getattr(settings, 'CACHE_CONTROL_MAX_AGE', 2592000)
		response['Cache-Control'] = 'max-age=%d' % MAX_AGE
		return response
		#return JsonResponse(complain_object_context_list,safe=False)

@csrf_exempt
def see_particular_complain(request):
	if request.method == "POST":
		#if not request.session.get('sessionid', None):
		#	return HttpResponse("Login required")
		#sessionid = request.session['sessionid']
		sessionid=json.loads(request.body.decode('utf-8'))['sessionid']

		try:
			total_complain_objects=Complain.objects.filter(sid=sessionid)
		except Exception as e:
			print("In the see complain object"+str(e))
		complain_object_context_list=[]
		if total_complain_objects:
			for complain_object in total_complain_objects[::-1]:
				complain_object_context={}

				try:
					complain_status_object = Complain_Status.objects.filter(cid=complain_object.cid)
					totalvotes=len(complain_status_object)
				except Exception as e:
					print("No total vote present")
					totalvotes=0
				'''try:
					my_complain_status_object=Complain_Status.objects.get(cid=complain_object.cid,aid=sessionid)
					myvoting=my_complain_status_object.voting
					#newmyvoting

				except Exception as e:
					print("My complain status is not present "+str(e))
					myvoting=0
				'''
				complain_owner_object=Student_details.objects.get(sid=complain_object.sid)

				complain_object_context['cid'] = complain_object.cid
				complain_object_context['sid'] = str(Students(complain_object.sid))
				complain_object_context['severity_of_punishment']=calculate_punishment(int(complain_object.cid))
				#complain_object_context['severity_of_punishment']=""
				complain_object_context['student_name'] = complain_owner_object.name
				complain_object_context['mobile_no'] = complain_owner_object.mobile_no
				complain_object_context['g_mobile_no'] = complain_owner_object.g_mobile_no
				complain_object_context['complain_txt'] = complain_object.complain_txt
				complain_object_context['attachment'] = complain_object.attachment
				complain_object_context['latitude']=str(complain_object.latitude)
				complain_object_context['longitude']=str(complain_object.longitude)
				complain_object_context['date'] = complain_object.date.strftime("%d-%m-%Y %I:%M %p")
				complain_object_context['status'] = complain_object.status
				complain_object_context['totalvotes']=totalvotes
				#if type(myvoting) is not int:
				#	complain_object_context['myvote']=int(myvoting)
				#else:
				complain_object_context['myvote'] = "0"
				complain_object_context_list.append(complain_object_context)
			#complain_object_context_list.append({'return_status':1})

		#else:
		#	complain_object_context_list.append({'return_status':2})
		response=JsonResponse(complain_object_context_list,safe=False)
		MAX_AGE = getattr(settings, 'CACHE_CONTROL_MAX_AGE', 2592000)
		response['Cache-Control'] = 'max-age=%d' % MAX_AGE
		return response
		#return JsonResponse(complain_object_context_list,safe=False)



'''
@csrf_exempt
def faltu(request):
	if request.method=="POST":
		#if not request.session.get('sessionid',None):
		#	return HttpResponse("Login Required")
		#sessionid=request.session['sessionid']
		#cid=json.loads(request.body.decode("utf-8"))['cid']
		try:
			allcomplainobject=Complain.objects.all()
		except Exception as e:
			print("All the complain objects are "+str(e))

		for complainobject in allcomplainobject:
			if type(complainobject.myvote) is str:
				complainobject.delete()

		return JsonResponse({'message':'object deleted successfully'})
'''

@csrf_exempt
def delete_complain(request):
	if request.method == "POST":
		#if not request.session.get('sessionid', None):
		#	return HttpResponse("Login required")
		#sessionid = request.session['sessionid']
		sessionid=json.loads(request.body.decode("utf-8"))['sessionid']
		cid=json.loads(request.body.decode("utf-8"))['cid']
		student_obj=Student_details.objects.get(sid=sessionid)

		name=student_obj.name

		try:
			complain_object=Complain.objects.get(cid=cid,sid=sessionid)
			notification_authority = "The complain of " + complain_object.complain_txt + " has been revoked by " + name
			complain_object.delete()
		except Exception as e:
			print("Delete complain object"+str(e))


		notification_student="The complain is removed successfully"
		current_date = datetime.datetime.now().strftime('%H:%M:%S')

		try:
			student_notification_object = Notification_Students(sid=Students(sessionid), notification=notification_student,
																date=current_date)
		except Exception as e:
			print("Add notification" + str(e))

		with transaction.atomic():
			student_notification_object.save()
			#For entering Notification to authorities in bulk amount

			authority_objects=Authority.objects.all()
			authority_object_list=[]
			for authority in authority_objects:
				aid=authority.aid
				particular_authority_object=Notification_Authority(aid=Authority(aid),notification=notification_authority,date=current_date)
				authority_object_list.append(particular_authority_object)

			Notification_Authority.objects.bulk_create(authority_object_list)

		return JsonResponse({'message':"Complain has been deleted successfully"},safe=False)

@csrf_exempt
def share_complain(request):
	if request.method =="POST":
		#if not request.session.get('sessionid',None):
		#	return HttpResponse("Login Required")
		#sessionid=request.session['sessionid']
		cid=json.loads(request.body.decode("utf-8"))['cid']
		#severity=calculate_punishment(cid)
		severity=""
		try:
			complain_object=Complain.objects.get(cid=cid)
		except Exception as e:
			print("share complain is "+str(e))
		status=complain_object.status
		text=complain_object.complain_txt
		attachment=complain_object.attachment
		sid=complain_object.sid
		current_date=complain_object.date
		latitude=complain_object.latitude
		longitude=complain_object.longitude
		#complain_status_object = Complain_Status.objects.filter(cid=complain_object.cid)
		totalvotes = 0
		myvoting = 0

		try:
			new_complain_obj=Complain(sid=sid,complain_txt=text,attachment=attachment,date=current_date,status=status)
		except Exception as e:
			print("In the new complain share status "+str(e))
		new_complain_obj.save()
		try:
			new_complain_object = Complain.objects.order_by("-cid").first()
		except Exception as e:
			print("In the complain " + str(e))
		try:
			complain_owner_object = Student_details.objects.get(sid=sid)
		except Exception as e:
			print("In the complain owner object" + str(e))

		complain_object_context = {}

		complain_object_context['cid'] = new_complain_object.cid
		complain_object_context['sid'] = str(new_complain_object.sid)
		complain_object_context['student_name'] = complain_owner_object.name
		complain_object_context['mobile_no'] = complain_owner_object.mobile_no
		complain_object_context['g_mobile_no'] = complain_owner_object.g_mobile_no
		complain_object_context['complain_txt'] = text
		complain_object_context['attachment'] = attachment
		complain_object_context['date'] = current_date
		complain_object_context['totalvotes']=totalvotes
		complain_object_context['myvote']=myvoting
		complain_object_context['status'] = status
		complain_object_context['severity_of_punishment']=severity
		complain_object_context['latitude']=str(latitude)
		complain_object_context['longitude']=str(longitude)

		return JsonResponse(complain_object_context,safe=False)

@csrf_exempt
def voting_complain(request):
	if request.method == "POST":
		#if not request.session.get('sessionid', None):
		#	return HttpResponse("Login required")
		#sessionid = request.session['sessionid']
		sessionid=json.loads(request.body.decode("utf-8"))['sessionid']
		cid=json.loads(request.body.decode("utf-8"))['cid']
		voting=json.loads(request.body.decode("utf-8"))['voting']

		try:
			voting_object=Complain_Status.objects.get(aid=sessionid,cid=cid)
			voting_object.voting = voting
			voting_object.save()
		except Exception as e:
			print("In voting complains " +str(e))
			#voting_object=None
			add_voting_object=Complain_Status(cid=Complain(cid),aid=Authority(sessionid),voting=voting)
			add_voting_object.save()

		try:
			new_voting_object=Complain_Status.objects.get(aid=sessionid,cid=cid)
		except Exception as e:
			print("In voting "+str(e))
		severity=calculate_punishment(cid)
		try:
			complain_status_object = Complain_Status.objects.filter(cid=cid)
			totalvotes = len(complain_status_object)
		except Exception as e:
			print("No total vote present")
			totalvotes = 0
		context_voting_object={}
		context_voting_object['aid']=sessionid
		context_voting_object['cid']=cid
		context_voting_object['voting']=str(new_voting_object.voting)
		context_voting_object['severity']=severity
		context_voting_object['totalvotes']=totalvotes


		return JsonResponse(context_voting_object,safe=False)

@csrf_exempt
@cache_control(must_revalidate=False, max_age=60*60*24)
@cache_page(60 * 15)
def see_student_notification(request):
	if request.method=="POST":
		#if not request.session.get('sessionid', None):
		#	return HttpResponse("Login required")
		sessionid = json.loads(request.body.decode('utf-8'))['sessionid']
		#sessionid=7
		print("The sessionid is",str(sessionid))
		try:
			student_notification_objects=Notification_Students.objects.filter(sid=sessionid)
		except Exception as e:
			print("See student notification "+str(e))

		context_student_notification_list=[]
		if student_notification_objects:
			for student_notification in student_notification_objects[::-1]:
				context_student_notification={}
				context_student_notification['nsid']=student_notification.nsid
				context_student_notification['sid']=str(student_notification.sid)
				context_student_notification['notification']=student_notification.notification
				context_student_notification['date']=student_notification.date
				context_student_notification_list.append(context_student_notification)
			context_student_notification_list.append({'return_status':1})
			#return JsonResponse(context_student_notification_list,safe=False)
		else:
			context_student_notification_list.append({'return_status': 2})
		response=JsonResponse(context_student_notification_list,safe=False)
		MAX_AGE = getattr(settings, 'CACHE_CONTROL_MAX_AGE', 2592000)
		response['Cache-Control'] = 'max-age=%d' % MAX_AGE
		return response
		#return JsonResponse(context_student_notification_list,safe=False)



@csrf_exempt
def delete_student_notification(request):
	if request.method == "POST":
		#if not request.session.get('sessionid', None):
		#	return HttpResponse("Login required")

		#sessionid = request.session['sessionid']
		sessionid = json.loads(request.body.decode('utf-8'))['sessionid']
		nsid = json.loads(request.body.decode('utf-8'))['nsid']
		#For Deleting particular Notification
		if nsid !="-1":
			try:
				student_notification_object = Notification_Students.objects.get(sid=sessionid, nsid=nsid)
			except Exception as e:
				print("In the delete authority notification" + str(e))
			student_notification_object.delete()
		#For deleting All Notification
		elif nsid=="-1":
			try:
				student_notifications_object = Notification_Students.objects.filter(sid=sessionid)
			except Exception as e:
				print("In the delete authority notification" + str(e))
			student_notifications_object.delete()

		return JsonResponse({'return_status': 2, 'sessionuserid': sessionid}, safe=False)


@csrf_exempt
def see_authority_notification(request):
	if request.method=="POST":
		#if not request.session.get('sessionid', None):
		#	return HttpResponse("Login required")
		#sessionid = request.session['sessionid']
		sessionid = json.loads(request.body.decode('utf-8'))['sessionid']
		try:
			authority_notification_objects=Notification_Authority.objects.filter(aid=sessionid)
		except Exception as e:
			print("See authority notification "+str(e))

		context_authority_notification_list=[]
		if authority_notification_objects:
			for authority_notification in authority_notification_objects[::-1]:
				context_authority_notification={}
				context_authority_notification['nid']=authority_notification.nid
				context_authority_notification['aid']=str(authority_notification.aid)
				context_authority_notification['notification']=authority_notification.notification
				context_authority_notification['date']=authority_notification.date
				context_authority_notification_list.append(context_authority_notification)
			context_authority_notification_list.append({'return_status':1})

		else:
			context_authority_notification_list.append({'return_status': 2})
		#return JsonResponse(context_authority_notification_list,safe=False)
		response=JsonResponse(context_authority_notification_list,safe=False)
		MAX_AGE = getattr(settings, 'CACHE_CONTROL_MAX_AGE', 2592000)
		response['Cache-Control'] = 'max-age=%d' % MAX_AGE
		return response
@csrf_exempt
def delete_authority_notification(request):
	if request.method=="POST":
		#if not request.session.get('sessionid', None):
		#	return HttpResponse("Login required")
		#sessionid = request.session['sessionid']
		sessionid = json.loads(request.body.decode('utf-8'))['sessionid']
		nid=json.loads(request.body.decode('utf-8'))['nid']
		#For deleting Particular Notification
		if nid!="-1":
			try:
				authority_notification_object=Notification_Authority.objects.get(aid=sessionid,nid=nid)
			except Exception as e:
				print("In the delete authority notification"+str(e))
			authority_notification_object.delete()

		#For Deleting All Notifications
		elif nid=="-1":
			try:
				authority_notifications_object = Notification_Authority.objects.filter(aid=sessionid)
			except Exception as e:
				print("In the delete authority notification" + str(e))
			authority_notifications_object.delete()

		return JsonResponse({'return_status':2,'sessionuserid':sessionid},safe=False)



@csrf_exempt
def calculate_punishment(cid):
	#print("In severity of punishment")
	complain_status_object1=Complain_Status.objects.filter(cid=cid,voting="1")
	complain_status_object2=Complain_Status.objects.filter(cid=cid,voting="2")
	complain_status_object3=Complain_Status.objects.filter(cid=cid,voting="3")
	complain_status_object4=Complain_Status.objects.filter(cid=cid,voting="4")
	complain_status_object5=Complain_Status.objects.filter(cid=cid,voting="5")
	onevote=len(complain_status_object1)
	twovote=len(complain_status_object2)
	threevote=len(complain_status_object3)
	fourvote=len(complain_status_object4)
	fivevote=len(complain_status_object5)

	vote_threshold=onevote*20+twovote*30+threevote*50+fourvote*80+fivevote*100
	if vote_threshold<=1000:
		severity="D"
	elif vote_threshold > 1000 and vote_threshold<=2000:
		severity = "C"
	elif vote_threshold > 2000 and vote_threshold<=3000:
		severity = "B"
	elif vote_threshold >= 3000:
		severity = "A"

	return severity



