package cz.cvut.fit.pinadani.cardgamear.interactors;

/**
 * Interactor which communicate with <a href="http://firebase.com">Firebase service</a>
 *
 * @author Michal Kučera [michal.kucera@ackee.cz] 07/07/15
 */
public class FirebaseInteractorImpl implements IFirebaseInteractor {
    public static final String TAG = FirebaseInteractorImpl.class.getName();


    //    @Override
//    public Observable<String> createNewAccount(String email, String password) {
//        Observable.OnSubscribe<String> subscribe = subscriber -> mRefMaker
//                .baseRef()
//                .createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
//                    @Override
//                    public void onSuccess(Map<String, Object> stringObjectMap) {
//                        if (stringObjectMap.containsKey(Constants.UID)) {
//                            subscriber.onNext((String) stringObjectMap.get(Constants.UID));
//                        }
//                        subscriber.onCompleted();
//                    }
//
//                    @Override
//                    public void onError(FirebaseError firebaseError) {
//                        subscriber.onError(new SignUpError(firebaseError.getCode()));
//                        subscriber.onCompleted();
//                    }
//                });
//
//        return Observable.create(subscribe);
//    }

//    private final IUserPreferencesInteractor mUserPreferences;
//
//    private final FirebaseRefMakerService mRefMaker;
//    private Query mMessageObserverQuery;
//    private ChildEventListener mMessageObserver;
//    private Query mMessageSingleQuery;
//    private ValueEventListener mMessageSingle;
//    private Firebase mCategoriesSingleRef;
//    private ValueEventListener mCategoriesSingle;
//    private Firebase mConversationDetailObserverRef;
//    private ValueEventListener mConversationDetailObserver;
//    private Firebase mContactConversationDetailObserverRef;
//    private ValueEventListener mContactConversationDetailObserver;
//    private Firebase mConversationNameObserverRef;
//    private ValueEventListener mConversationNameObserver;
//    private Firebase mConversationObserverRef;
//    private Firebase mProfileHeadObserverRef;
//    private Firebase mUserConversationObserverRef;
//    private ChildEventListener mConversationObserver;
//    private ValueEventListener mConversationFirstTimeObserver;
//    private Firebase mConversationFirstTimeObserverRef;
//    private ChildEventListener mLastMessageListener;
//    private ValueEventListener mProfileHeadListener;
//    private Firebase mLeaveConversationRef;
//
//
//    public FirebaseInteractorImpl(IUserPreferencesInteractor userPreferences,
//                                  FirebaseRefMakerService baseRef) {
//        mUserPreferences = userPreferences;
//        mRefMaker = baseRef;
//    }
//
//    @Override
//    public Observable<List<User>> loadContactsSingle() {
//        Observable.OnSubscribe<List<User>> subscribe = subscriber -> {
//            // Retrieve ref
//            Firebase ref = mRefMaker.refForUserFriends(mUserPreferences.getUserId());
//            // Request for data
//            ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
//                    List<User> list = new ArrayList<>();
//                    for (DataSnapshot snapshot : iterable) {
//                        UserHead userHead = snapshot.getValue(UserHead.class);
//                        User user = new User();
//                        user.setInformation(userHead);
//                        user.setId(snapshot.getKey());
//                        list.add(user);
//                    }
//                    subscriber.onNext(list);
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                    subscriber.onCompleted();
//                }
//            });
//
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<User> deleteContact(User selectedContact) {
//        Observable.OnSubscribe<User> subscribe = subscriber -> {
//
//            subscriber.onNext(null);
//            subscriber.onCompleted();
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Conversation> loadConversations() {
//        Observable.OnSubscribe<Conversation> subscribe = subscriber -> {
//            String userId = mUserPreferences.getUserId();
//            mConversationObserverRef = mRefMaker.refForUserConversations(userId);
//            mConversationObserver = new ChildEventListener() {
//
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String identifier) {
//                    dispatchConversation(subscriber, dataSnapshot);
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String identifier) {
//                    dispatchConversation(subscriber, dataSnapshot);
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                    dispatchConversation(subscriber, dataSnapshot, true);
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                    subscriber.onCompleted();
//                }
//            };
//
//            mConversationObserverRef.addChildEventListener(mConversationObserver);
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Conversation> loadLastMessage(Context context, Conversation conversation) {
//        Observable.OnSubscribe<Conversation> subscribe = subscriber -> {
//
//            Firebase ref = mRefMaker.refForMessages(conversation.getId());
//            mMessageSingleQuery = ref.orderByKey().limitToLast(1);
//            mLastMessageListener = new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String previousChild) {
//
//                    Message message = dataSnapshot.getValue(Message.class);
//
//                    if (message.getFlag() == Message.TYPE_USER_IMAGE) {
//                        if (message.getUser().equals(mUserPreferences.getUserId())) {
//                            conversation.setLastMessage(context.getString(R.string.message_image_from_user));
//                        } else {
//                            conversation.setLastMessage(context.getString(R.string.message_image_from_contact));
//                        }
//
//                    }
//                    subscriber.onNext(conversation);
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                    Message message = dataSnapshot.getValue(Message.class);
//
//                    if (message.getFlag() == Message.TYPE_USER_IMAGE) {
//                        if (message.getUser().equals(mUserPreferences.getUserId())) {
//                            conversation.setLastMessage(context.getString(R.string.message_image_from_user));
//                        } else {
//                            conversation.setLastMessage(context.getString(R.string.message_image_from_contact));
//                        }
//
//                    }
//                    subscriber.onNext(conversation);
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                    subscriber.onCompleted();
//                }
//            };
//            mMessageSingleQuery.addChildEventListener(mLastMessageListener);
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Conversation> loadConversationName(Conversation conversation) {
//        Observable.OnSubscribe<Conversation> subscribe = subscriber -> {
//            mConversationNameObserverRef = mRefMaker.refForConversationDetail(conversation.getId());
//            if(!TextUtils.isEmpty(conversation.getName())){
//                subscriber.onNext(conversation);
//                subscriber.onCompleted();
//            } else {
//                mConversationNameObserver = new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Conversation newConversation = dataSnapshot.getValue(Conversation.class);
//                        if (newConversation != null && !TextUtils.isEmpty(newConversation.getName())) {
//                            conversation.setName(newConversation.getName());
//                        }
//                        if (newConversation != null && newConversation.getParticipants() != null) {
//                            conversation.setParticipants(newConversation.getParticipants());
//                        }
//                        subscriber.onNext(conversation);
//                        subscriber.onCompleted();
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//                        subscriber.onError(firebaseError.toException());
//                        subscriber.onCompleted();
//                    }
//                };
//
//                mConversationNameObserverRef.addListenerForSingleValueEvent(mConversationNameObserver);
//            }
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    private void dispatchConversation(Subscriber<? super Conversation> subscriber,
//                                      DataSnapshot dataSnapshot) {
//        dispatchConversation(subscriber, dataSnapshot, false);
//    }
//
//    private void dispatchConversation(Subscriber<? super Conversation> subscriber,
//                                      DataSnapshot dataSnapshot,
//                                      boolean isDeleted) {
//
//        Conversation conversation = dataSnapshot.getValue(Conversation.class);
//        String key = dataSnapshot.getKey();
//        conversation.setId(key);
//        conversation.setIsDeleted(isDeleted);
//        subscriber.onNext(conversation);
//    }
//
//    @Override
//    public Observable<Message> sendMessage(Message message) {
//        Observable.OnSubscribe<Message> subscribe = subscriber -> {
//
//            Firebase ref = mRefMaker.refForMessages(message.getConversation());
//            ref.push().setValue(message, (firebaseError, firebase) -> {
//                if (firebaseError != null) {
//                    subscriber.onError(firebaseError.toException());
//                } else {
//                    subscriber.onNext(message);
//                }
//                subscriber.onCompleted();
//            });
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<AuthData> login(CharSequence email, CharSequence password) {
//
//        Observable.OnSubscribe<AuthData> subscribe = subscriber -> {
//            Firebase baseRef = mRefMaker.baseRef();
//            baseRef.authWithPassword(String.valueOf(email),
//                    String.valueOf(password),
//                    new Firebase.AuthResultHandler() {
//                        @Override
//                        public void onAuthenticated(AuthData authData) {
//                            subscriber.onNext(authData);
//                            subscriber.onCompleted();
//                        }
//
//                        @Override
//                        public void onAuthenticationError(FirebaseError firebaseError) {
//                            subscriber.onError(new SignUpError(firebaseError.getCode()));
//                            subscriber.onCompleted();
//                        }
//                    });
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<User> loadProfile() {
//
//        Observable.OnSubscribe<User> subscribe = subscriber -> {
//            Firebase profile = mRefMaker.refForUser(mUserPreferences.getUserId());
//            profile.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.getValue() == null) {
//                        subscriber.onNext(null);
//                        subscriber.onCompleted();
//                        return;
//                    }
//                    User user = dataSnapshot.getValue(User.class);
//                    user.setId(dataSnapshot.getKey());
//                    subscriber.onNext(user);
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                    subscriber.onCompleted();
//                }
//            });
//        };
//        return Observable.create(subscribe);
//
//    }
//
//    @Override
//    public Observable<User> loadUser(User user) {
//
//        Observable.OnSubscribe<User> subscribe = subscriber -> {
//            Firebase profile = mRefMaker.refForUser(user.getId());
//            profile.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    User newUser = dataSnapshot.getValue(User.class);
//                    newUser.setId(dataSnapshot.getKey());
//                    newUser.setLatitude(user.getLatitude());
//                    newUser.setLongitude(user.getLongitude());
//                    subscriber.onNext(newUser);
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                    subscriber.onCompleted();
//                }
//            });
//        };
//        return Observable.create(subscribe);
//
//    }
//
//    @Override
//    public Observable<User> updateProfile(User user) {
//        Observable.OnSubscribe<User> subscribe = subscriber -> {
//            Firebase profile = mRefMaker.refForUser(mUserPreferences.getUserId());
//            profile.setValue(user, (firebaseError, firebase) -> {
//                if (firebaseError == null) {
//                    subscriber.onNext(user);
//                    subscriber.onCompleted();
//                } else {
//                    subscriber.onError(firebaseError.toException());
//                    subscriber.onCompleted();
//                }
//            });
//        };
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<String> deleteVisibility(boolean isCompany) {
//
//        Observable.OnSubscribe<String> subscribe = subscriber -> {
//            GeoFire geoFire;
//            if (isCompany) {
//                geoFire = new GeoFire(mRefMaker.refForServiceLocator());
//            } else {
//                geoFire = new GeoFire(mRefMaker.refForUserLocator());
//            }
//            geoFire.removeLocation(mUserPreferences.getUserId(), (key, error) -> {
//                if (error == null) {
//                    subscriber.onNext(key);
//                } else {
//                    subscriber.onError(error.toException());
//                }
//                subscriber.onCompleted();
//            });
//        };
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<User> updateProfileHead(User user, int state) {
//        UserHead head = new UserHead();
//        head.setAvatar(user.getAvatar());
//        head.setUsername(user.getUsername());
//        head.setState(state);
//        head.setConversation(user.getConversation());
//        user.setState(state);
//
//        Observable.OnSubscribe<User> subscribe = subscriber -> {
//            Firebase ref = mRefMaker.refForUserFriend(mUserPreferences.getUserId(), user.getId());
//            if (state == 0) {
//                ref.removeValue((firebaseError, firebase) -> {
//                    if (firebaseError == null) {
//                        subscriber.onNext(user);
//                    } else {
//                        subscriber.onError(firebaseError.toException());
//                    }
//                    subscriber.onCompleted();
//                });
//            } else {
//                ref.setValue(head, (firebaseError, firebase) -> {
//                    if (firebaseError == null) {
//                        subscriber.onNext(user);
//                    } else {
//                        subscriber.onError(firebaseError.toException());
//                    }
//                    subscriber.onCompleted();
//                });
//            }
//        };
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<User> updateContactHead(User user, int state) {
//        UserHead head = new UserHead();
//        head.setAvatar(mUserPreferences.getAvatar());
//        head.setUsername(mUserPreferences.getUsername());
//        head.setState(state);
//        head.setConversation(user.getConversation());
//        user.setState(state);
//
//        Observable.OnSubscribe<User> subscribe = subscriber -> {
//            Firebase ref = mRefMaker.refForContactFriend(mUserPreferences.getUserId(), user.getId());
//            if (state == 0) {
//                ref.removeValue((firebaseError, firebase) -> {
//                    if (firebaseError == null) {
//                        subscriber.onNext(user);
//                    } else {
//                        subscriber.onError(firebaseError.toException());
//                    }
//                    subscriber.onCompleted();
//                });
//            } else {
//                ref.setValue(head, (firebaseError, firebase) -> {
//                    if (firebaseError == null) {
//                        subscriber.onNext(user);
//                    } else {
//                        subscriber.onError(firebaseError.toException());
//                    }
//                    subscriber.onCompleted();
//                });
//            }
//        };
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<List<User>> findNearbyContacts(Location location, float distance, boolean company) {
//        final List<User> nearbyUser = new ArrayList<>();
//        GeoFire geoFire;
//        if (company) {
//            geoFire = new GeoFire(mRefMaker.refForServiceLocator());
//        } else {
//            geoFire = new GeoFire(mRefMaker.refForUserLocator());
//        }
//        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), distance);
//
//        Observable.OnSubscribe<List<User>> subscribe = subscriber -> geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
//            @Override
//            public void onKeyEntered(String id, GeoLocation location) {
//                User newUser = new User();
//                newUser.setId(id);
//                newUser.setLatitude(location.latitude);
//                newUser.setLongitude(location.longitude);
//                nearbyUser.add(newUser);
//            }
//
//            @Override
//            public void onKeyExited(String username) {
//            }
//
//            @Override
//            public void onKeyMoved(String key, GeoLocation location) {
//            }
//
//            @Override
//            public void onGeoQueryReady() {
//                subscriber.onNext(nearbyUser);
//                subscriber.onCompleted();
//            }
//
//            @Override
//            public void onGeoQueryError(FirebaseError error) {
//                subscriber.onError(error.toException());
//                subscriber.onCompleted();
//            }
//        });
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public void markConversationAsRead(String conversationId) {
//
//        Firebase ref = mRefMaker
//                .refForUserConversation(mUserPreferences.getUserId(), conversationId)
//                .child(FirebaseRefMakerService.LEAF_READ);
//
//        ref.setValue(true);
//    }
//
//    @Override
//    public Observable<User> updateContact(String contact) {
//        Observable.OnSubscribe<User> subscribe = subscriber -> {
//            Firebase ref = mRefMaker.refForUser(contact);
//
//            ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    User user = dataSnapshot.getValue(User.class);
//                    user.setId(contact);
//                    subscriber.onNext(user);
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                    subscriber.onCompleted();
//                }
//            });
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Object> createProfilePhoto(Bitmap profilePhoto) {
//        Observable.OnSubscribe<Object> subscribe = subscriber -> {
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Object> deleteProfilePhoto(String profilePhotoId) {
//
//        return null;
//    }
//
//    @Override
//    public Observable<List<Message>> loadSingleMessages(String conversationId, String lastId) {
//        Observable.OnSubscribe<List<Message>> subscribe = subscriber -> {
//
//            Firebase ref = mRefMaker.refForMessages(conversationId);
//            mMessageSingleQuery = ref.orderByKey().endAt(lastId).limitToLast(Constants.NUMBER_LOAD_MESSAGES);
//            mMessageSingle = new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
//                    List<Message> list = new ArrayList<>();
//                    for (DataSnapshot snapshot : iterable) {
//                        Message message = snapshot.getValue(Message.class);
//                        message.setId(snapshot.getKey());
//                        list.add(message);
//                    }
//                    list.remove(list.size() - 1);
//                    subscriber.onNext(list);
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                    subscriber.onCompleted();
//                }
//            };
//
//            mMessageSingleQuery.addListenerForSingleValueEvent(mMessageSingle);
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<List<LocatorCategory>> loadSingleCategories() {
//        Observable.OnSubscribe<List<LocatorCategory>> subscribe = subscriber -> {
//
//            mCategoriesSingleRef = mRefMaker.refForCategories();
//            mCategoriesSingle = new ValueEventListener() {
//
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
//                    List<LocatorCategory> list = new ArrayList<>();
//                    for (DataSnapshot snapshot : iterable) {
//                        LocatorCategory category = new LocatorCategory((String) snapshot.getKey(),
//                                (String) snapshot.getValue());
//                        list.add(category);
//                    }
//                    subscriber.onNext(list);
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                    subscriber.onCompleted();
//                }
//            };
//
//            mCategoriesSingleRef.addListenerForSingleValueEvent(mCategoriesSingle);
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<List<LocatorCategory>> loadUserCategories(String userId) {
//        Observable.OnSubscribe<List<LocatorCategory>> subscribe = subscriber -> {
//
//            mCategoriesSingleRef = mRefMaker.refForUserCategory(userId);
//            mCategoriesSingle = new ValueEventListener() {
//
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
//                    List<LocatorCategory> list = new ArrayList<>();
//                    for (DataSnapshot snapshot : iterable) {
//                        LocatorCategory category = new LocatorCategory((String) snapshot.getKey(),
//                                (String) snapshot.getValue());
//                        list.add(category);
//                    }
//                    subscriber.onNext(list);
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                    subscriber.onCompleted();
//                }
//            };
//
//            mCategoriesSingleRef.addListenerForSingleValueEvent(mCategoriesSingle);
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Message> loadObservableMessages(String conversationId) {
//
//        Observable.OnSubscribe<Message> subscribe = subscriber -> {
//
//            Firebase ref = mRefMaker.refForMessages(conversationId);
//            mMessageObserverQuery = ref.orderByKey().limitToLast(Constants.NUMBER_LOAD_MESSAGES);
//            mMessageObserver = new ChildEventListener() {
//
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    Message message = dataSnapshot.getValue(Message.class);
//                    message.setId(dataSnapshot.getKey());
//
//                    subscriber.onNext(message);
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                    Message message = dataSnapshot.getValue(Message.class);
//                    message.setId(dataSnapshot.getKey());
//
//                    subscriber.onNext(message);
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                }
//            };
//
//            mMessageObserverQuery.addChildEventListener(mMessageObserver);
//        };
//
//        return Observable.create(subscribe);
//    }
//
//
//    @Override
//    public Observable<Conversation> loadObservableConversation(String conversationId) {
//        Observable.OnSubscribe<Conversation> subscribe = subscriber -> {
//            mConversationDetailObserverRef = mRefMaker.refForConversationDetail(conversationId);
//
//            mConversationDetailObserver = new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if(dataSnapshot.getValue() == null){
//                        subscriber.onNext(null);
//                        subscriber.unsubscribe();
//                        return;
//                    }
//                    Conversation conversation = dataSnapshot.getValue(Conversation.class);
//                    conversation.setId(dataSnapshot.getKey());
//
//                    subscriber.onNext(conversation);
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                }
//            };
//
//            mConversationDetailObserverRef.addValueEventListener(mConversationDetailObserver);
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    public Observable<Void> changeTypingStatus(String conversationId,
//                                               String userId,
//                                               String username,
//                                               boolean isTyping) {
//
//        Observable.OnSubscribe<Void> subscribe = subscriber -> {
//            Firebase.CompletionListener completionListener = (firebaseError, firebase) -> {
//                if (firebaseError != null) {
//                    subscriber.onError(firebaseError.toException());
//                } else {
//                    subscriber.onNext(null);
//                }
//                subscriber.onCompleted();
//            };
//
//            Firebase ref = mRefMaker.refForConversationDetailTyping(conversationId);
//            // Get child with userId
//            ref = ref.child(userId);
//            if (isTyping) {
//                ref.setValue(username, completionListener);
//            } else {
//                ref.removeValue(completionListener);
//            }
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Void> changeConversationName(Conversation conversation, CharSequence text) {
//        Observable.OnSubscribe<Void> subscribe = subscriber -> {
//            Map<String, Object> changes = new HashMap<>();
//            for (String userId : conversation.getParticipants().keySet()) {
//                changes.put(mRefMaker.refForUserConversationName(userId, conversation.getId()),
//                        text.toString());
//            }
//            changes.put(mRefMaker.refForUserConversationName(mUserPreferences.getUserId(), conversation.getId()),
//                    text.toString());
//
//            changes.put(mRefMaker.refForConversationName(conversation.getId()),
//                    text.toString());
//
//            Firebase ref = mRefMaker.baseRef();
//
//            ref.updateChildren(changes, new Firebase.CompletionListener() {
//                @Override
//                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
//                    subscriber.onNext(null);
//                    subscriber.onCompleted();
//                }
//            });
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Void> makeChanges(Map<String, Object> changes) {
//        Observable.OnSubscribe<Void> subscribe = subscriber -> {
//            Firebase ref = mRefMaker.baseRef();
//            ref.updateChildren(changes, (firebaseError, firebase) -> {
//                subscriber.onNext(null);
//                subscriber.onCompleted();
//            });
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Void> uploadCategory(String userId, Map<String, String> category) {
//        Observable.OnSubscribe<Void> subscribe = subscriber -> {
//            Firebase ref = mRefMaker.refForUserCategory(userId);
//            ref.setValue(category, (firebaseError, firebase) -> {
//                if (firebaseError != null) {
//                    subscriber.onError(firebaseError.toException());
//                } else {
//                    subscriber.onNext(null);
//                }
//                subscriber.onCompleted();
//            });
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Void> updateUserConversation(String conversationId,
//                                                   Map<String, Object> updates) {
//        Observable.OnSubscribe<Void> subscribe = subscriber -> {
//            Firebase ref = mRefMaker
//                    .refForUserConversation(mUserPreferences.getUserId(), conversationId);
//
//            ref.updateChildren(updates, (firebaseError, firebase) -> {
//                if (firebaseError == null) {
//                    subscriber.onNext(null);
//                } else {
//                    subscriber.onError(firebaseError.toException());
//                }
//                subscriber.onCompleted();
//            });
//        };
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public void clearMessageObserverListeners() {
//        if (mMessageObserverQuery != null && mMessageObserver != null) {
//            mMessageObserverQuery.removeEventListener(mMessageObserver);
//        }
//        if (mConversationDetailObserverRef != null && mConversationDetailObserver != null) {
//            mConversationDetailObserverRef.removeEventListener(mConversationDetailObserver);
//        }
//        if (mContactConversationDetailObserverRef != null && mContactConversationDetailObserver != null) {
//            mContactConversationDetailObserverRef.removeEventListener(mContactConversationDetailObserver);
//        }
//    }
//
//    @Override
//    public void clearConversationObserverListeners() {
//        if (mConversationObserverRef != null && mConversationObserver != null) {
//            mConversationObserverRef.removeEventListener(mConversationObserver);
//        }
//    }
//
//    @Override
//    public void clearLastMessageListeners() {
//        if (mMessageSingleQuery != null && mLastMessageListener != null) {
//            mMessageSingleQuery.removeEventListener(mLastMessageListener);
//        }
//
//    }
//
//    @Override
//    public void clearProfileHeadListener() {
//        if (mProfileHeadListener != null && mProfileHeadListener != null) {
//            mProfileHeadObserverRef.removeEventListener(mProfileHeadListener);
//        }
//    }
//

//
//    @Override
//    public Observable<Void> pushUserData(String uid, User user) {
//
//        Observable.OnSubscribe<Void> subscribe = subscriber -> mRefMaker
//                .refForUser(uid)
//                .setValue(user, (firebaseError, firebase) -> {
//                    if (firebaseError != null) {
//                        subscriber.onError(new SignUpError(firebaseError.getCode()));
//                    } else {
//                        subscriber.onNext(null);
//                    }
//                    subscriber.onCompleted();
//                });
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<User> getProfileHead(User contact) {
//        Observable.OnSubscribe<User> subscribe = subscriber -> {
//            mProfileHeadObserverRef = mRefMaker.refForUserFriend(mUserPreferences.getUserId(), contact.getId());
//            mProfileHeadObserverRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    UserHead userHead = dataSnapshot.getValue(UserHead.class);
//                    if (userHead != null) {
//                        contact.setInformation(userHead);
//                    }
//                    subscriber.onNext(contact);
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                    subscriber.onCompleted();
//                }
//            });
//        };
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<User> getContactHead(User contact) {
//        Observable.OnSubscribe<User> subscribe = subscriber -> {
//            mProfileHeadObserverRef = mRefMaker.refForContactFriend(mUserPreferences.getUserId(), contact.getId());
//            mProfileHeadObserverRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    UserHead userHead = dataSnapshot.getValue(UserHead.class);
//                    if (userHead != null) {
//                        contact.setInformation(userHead);
//                    }
//                    subscriber.onNext(contact);
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                    subscriber.onCompleted();
//                }
//            });
//        };
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Void> leaveConversation(Conversation conversation) {
//        Observable.OnSubscribe<Void> subscribe = subscriber -> {
//            mLeaveConversationRef = mRefMaker.refForConversationParticipant(conversation.getId(),
//                    mUserPreferences.getUserId());
//
//            mLeaveConversationRef.removeValue(new Firebase.CompletionListener() {
//                @Override
//                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
//                    subscriber.onNext(null);
//                    subscriber.onCompleted();
//                }
//            });
//        };
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Void> deleteConversation(Conversation conversation) {
//
//        Observable.OnSubscribe<Void> subscribe = subscriber -> {
//            mLeaveConversationRef = mRefMaker.refForUserConversationDetail(mUserPreferences.getUserId(),
//                    conversation.getId());
//
//            mLeaveConversationRef.removeValue(new Firebase.CompletionListener() {
//                @Override
//                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
//                    subscriber.onNext(null);
//                    subscriber.onCompleted();
//                }
//            });
//        };
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Void> deleteConversationFromFriend(Conversation conversation) {
//        Observable.OnSubscribe<Void> subscribe = subscriber -> {
//            if (!conversation.isGroup()) {
//                String contactId = conversation.getParticipants().keySet().iterator().next();
//                if (!TextUtils.isEmpty(contactId)) {
//                    mLeaveConversationRef = mRefMaker.refForContactFriendConversation(mUserPreferences.getUserId(),
//                            contactId);
//
//                    mLeaveConversationRef.removeValue(new Firebase.CompletionListener() {
//                        @Override
//                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
//                            subscriber.onNext(null);
//                            subscriber.onCompleted();
//                        }
//                    });
//                }
//            } else {
//                subscriber.onNext(null);
//                subscriber.onCompleted();
//            }
//        };
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Void> pushNotification(User contact, String pushNotificationType) {
//
//        PushNotification notification = new PushNotification();
//        notification.setType(pushNotificationType);
//        notification.setUserkey(contact.getId());
//        Observable.OnSubscribe<Void> subscribe = subscriber -> {
//            Firebase.CompletionListener completionListener = (firebaseError, firebase) -> {
//                if (firebaseError != null) {
//                    subscriber.onError(firebaseError.toException());
//                } else {
//                    subscriber.onNext(null);
//                }
//                subscriber.onCompleted();
//            };
//
//            Firebase ref = mRefMaker.refForPushNotiifcation();
//            ref.push().setValue(notification, completionListener);
//
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Void> forgotPassword(CharSequence email) {
//        Observable.OnSubscribe<Void> subscribe = subscriber -> {
//            Firebase ref = mRefMaker.baseRef();
//            ref.resetPassword(email.toString(), new Firebase.ResultHandler() {
//                @Override
//                public void onSuccess() {
//                    subscriber.onNext(null);
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onError(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                    subscriber.onCompleted();
//                }
//            });
//        };
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Void> changePassword(String email, CharSequence oldPassword, CharSequence newPassword) {
//        Observable.OnSubscribe<Void> subscribe = subscriber -> {
//            Firebase ref = mRefMaker.baseRef();
//            ref.changePassword(email, oldPassword.toString(), newPassword.toString(), new Firebase.ResultHandler() {
//                @Override
//                public void onSuccess() {
//                    subscriber.onNext(null);
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onError(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                    subscriber.onCompleted();
//                }
//            });
//        };
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Conversation> loadUserConversation(Conversation conversation) {
//        Observable.OnSubscribe<Conversation> subscribe = subscriber -> {
//            String userId = mUserPreferences.getUserId();
//            mUserConversationObserverRef = mRefMaker.refForUserConversation(userId, conversation.getId());
//            mUserConversationObserverRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.getValue() == null) {
//                        subscriber.onNext(null);
//                    } else {
//                        dispatchConversation(subscriber, dataSnapshot);
//                    }
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                    subscriber.onCompleted();
//                }
//            });
//        };
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<Conversation> loadObservableContactConversation(User contact, String conversationId) {
//        Observable.OnSubscribe<Conversation> subscribe = subscriber -> {
//            mContactConversationDetailObserverRef = mRefMaker.refForUserConversation(contact.getId(), conversationId);
//
//            mContactConversationDetailObserver = new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if(dataSnapshot.getValue() == null){
//                        subscriber.onNext(null);
//                        subscriber.unsubscribe();
//                        return;
//                    }
//                    Conversation conversation = dataSnapshot.getValue(Conversation.class);
//                    conversation.setId(dataSnapshot.getKey());
//
//                    subscriber.onNext(conversation);
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                }
//            };
//
//            mContactConversationDetailObserverRef.addValueEventListener(mContactConversationDetailObserver);
//        };
//
//        return Observable.create(subscribe);
//    }
//
//    @Override
//    public Observable<List<Conversation>> loadConversationsFirstTime() {
//        Observable.OnSubscribe<List<Conversation>> subscribe = subscriber -> {
//
//            String userId = mUserPreferences.getUserId();
//            mConversationFirstTimeObserverRef = mRefMaker.refForUserConversations(userId);
//            mConversationFirstTimeObserver = new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
//                    List<Conversation> list = new ArrayList<>();
//                    for (DataSnapshot snapshot : iterable) {
//                        Conversation conversation = snapshot.getValue(Conversation.class);
//                        conversation.setId(snapshot.getKey());
//                        list.add(conversation);
//                    }
//                    subscriber.onNext(list);
//                    subscriber.onCompleted();
//                }
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    subscriber.onError(firebaseError.toException());
//                    subscriber.onCompleted();
//                }
//            };
//
//            mConversationFirstTimeObserverRef.addListenerForSingleValueEvent(mConversationFirstTimeObserver);
//        };
//
//        return Observable.create(subscribe);
//    }
}
