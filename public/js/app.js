// ==================== CONFIGURATION ====================
const CONFIG = {
    // Detecta automáticamente el protocolo y host (funciona en local y en producción/Render)
    WS_URL: `${window.location.protocol === 'https:' ? 'wss:' : 'ws:'}//${window.location.host}/ws`,
    ICE_SERVERS: { iceServers: [
        { urls: 'stun:stun.relay.metered.ca:80' },
        { urls: 'turn:global.relay.metered.ca:80', username: 'f52e1a9b48820bf66de8e724', credential: 'YUC5D8C0K2lo/Z4G' },
        { urls: 'turn:global.relay.metered.ca:80?transport=tcp', username: 'f52e1a9b48820bf66de8e724', credential: 'YUC5D8C0K2lo/Z4G' },
        { urls: 'turn:global.relay.metered.ca:443', username: 'f52e1a9b48820bf66de8e724', credential: 'YUC5D8C0K2lo/Z4G' },
        { urls: 'turns:global.relay.metered.ca:443?transport=tcp', username: 'f52e1a9b48820bf66de8e724', credential: 'YUC5D8C0K2lo/Z4G' }
    ] },
    METERED_API_KEY: 'f52e1a9b48820bf66de8e724',
    METERED_DOMAIN: 'chihuchat.metered.live',
    CLOUDINARY: {
        CLOUD_NAME: 'dtkwn8jao',
        UPLOAD_PRESET: 'chat_uploads',
        UPLOAD_URL: 'https://api.cloudinary.com/v1_1/dtkwn8jao/auto/upload'
    },
    EMOJIS: ['😀','😃','😄','😁','😆','😅','🤣','😂','🙂','🙃','😉','😊','😇','🥰','😍','🤩','😘','😗','☺️','😚','😙','🥲','😋','😛','😜','🤪','😝','🤑','🗣️','🤭','🤫','🤔','🤐','🤨','😐','😑','😶','😏','😒','🙄','😬','🤥','😌','😔','😪','🤤','😴','😷','🤒','🤕','🤢','🤮','🤧','🥵','🥶','🥴','😵','🤯','🤠','🥳','🥸','😎','🤓','🧐','😕','😟','🙁','☹️','😮','😯','😲','😳','🥺','😦','😧','😨','😰','😥','😢','😭','😱','😖','😣','😞','😓','😩','😫','🥱','😤','😡','😠','🤬','👍','👎','👏','🙌','👐','🤝','🙏','💪','🎉','🎊','🎈','🎁','🏆','🥇','🥈','🥉','⭐','🌟','💫','✨','🔥','💯'],
    // Iconos SVG estilo WhatsApp
    ICONS: {
        EMOJI: '<svg viewBox="0 0 24 24"><path d="M9.153 11.603c.795 0 1.439-.879 1.439-1.962s-.644-1.962-1.439-1.962-1.439.879-1.439 1.962.644 1.962 1.439 1.962zm5.694 0c.795 0 1.439-.879 1.439-1.962s-.644-1.962-1.439-1.962-1.439.879-1.439 1.962.644 1.962 1.439 1.962zM12 2C6.477 2 2 6.477 2 12s4.477 10 10 10 10-4.477 10-10S17.523 2 12 2zm0 18c-4.418 0-8-3.582-8-8s3.582-8 8-8 8 3.582 8 8-3.582 8-8 8zm0-4c-2.21 0-4-1.343-4-3h2c0 .552 1.12 1 2 1s2-.448 2-1h2c0 1.657-1.79 3-4 3z"/></svg>',
        ATTACH: '<svg viewBox="0 0 24 24"><path d="M1.816 15.556v.002c0 1.502.584 2.912 1.646 3.972s2.472 1.647 3.974 1.647a5.58 5.58 0 003.972-1.645l9.547-9.548c.769-.768 1.147-1.767 1.058-2.817-.079-.968-.548-1.927-1.319-2.698-1.594-1.592-4.068-1.711-5.517-.262l-7.916 7.915c-.881.881-.792 2.25.214 3.261.959.958 2.423 1.053 3.263.215l5.511-5.512c.28-.28.267-.722.053-.936l-.244-.244c-.191-.191-.567-.349-.957.04l-5.506 5.506c-.18.18-.635.127-.976-.214-.098-.097-.576-.613-.213-.973l7.915-7.917c.818-.817 2.267-.699 3.23.262.5.501.802 1.1.849 1.685.051.573-.156 1.111-.589 1.543l-9.547 9.549a3.97 3.97 0 01-2.829 1.171 3.975 3.975 0 01-2.83-1.173 3.973 3.973 0 01-1.172-2.828c0-1.071.415-2.076 1.172-2.83l7.209-7.211c.157-.157.264-.579.028-.814L11.5 4.36a.572.572 0 00-.834.018l-7.205 7.207a5.577 5.577 0 00-1.645 3.971z"/></svg>',
        CAMERA: '<svg viewBox="0 0 24 24"><path d="M21 6.5l-4 4V7c0-.55-.45-1-1-1H4c-.55 0-1 .45-1 1v10c0 .55.45 1 1 1h12c.55 0 1-.45 1-1v-3.5l4 4v-11z"/></svg>',
        IMAGE: '<svg viewBox="0 0 24 24"><path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V5h14v14zm-5-7l-3 3.72L9 13l-3 4h12l-4-5z"/></svg>',
        VIDEO: '<svg viewBox="0 0 24 24"><path d="M17 10.5V7c0-.55-.45-1-1-1H4c-.55 0-1 .45-1 1v10c0 .55.45 1 1 1h12c.55 0 1-.45 1-1v-3.5l4 4v-11l-4 4z"/></svg>',
        CALL: '<svg viewBox="0 0 24 24"><path d="M20.01 15.38c-1.23 0-2.42-.2-3.53-.56a.977.977 0 00-1.01.24l-1.57 1.97c-2.83-1.35-5.48-3.9-6.89-6.83l1.95-1.66c.27-.28.35-.67.24-1.02-.37-1.11-.56-2.3-.56-3.53 0-.54-.45-.99-.99-.99H4.19C3.65 3 3 3.24 3 3.99 3 13.28 10.73 21 20.01 21c.71 0 .99-.63.99-1.18v-3.45c0-.54-.45-.99-.99-.99z"/></svg>',
        SEND: '<svg viewBox="0 0 24 24"><path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/></svg>',
        MIC: '<svg viewBox="0 0 24 24"><path d="M12 14c1.66 0 3-1.34 3-3V5c0-1.66-1.34-3-3-3S9 3.34 9 5v6c0 1.66 1.34 3 3 3zm5.91-3c-.49 0-.9.36-.98.85C16.52 14.2 14.47 16 12 16s-4.52-1.8-4.93-4.15a.998.998 0 00-.98-.85c-.61 0-1.09.54-1 1.14.49 3 2.89 5.35 5.91 5.78V20c0 .55.45 1 1 1s1-.45 1-1v-2.08c3.02-.43 5.42-2.78 5.91-5.78.1-.6-.39-1.14-1-1.14z"/></svg>'
    }
};

// ==================== APPLICATION STATE ====================
const AppState = {
    currentUser: null,
    currentUserProfilePicture: null,
    contacts: [],
    groups: [],
    activeChat: null,
    activeChatType: null,
    conversations: {},
    currentView: 'contacts',
    
    // WebRTC state
    localStream: null,
    peerConnection: null,
    incomingCallData: null,
    callStartTime: null,
    callDurationInterval: null,
    audioEnabled: true,
    videoEnabled: true
};

// ==================== AUTHENTICATION MANAGER ====================
const AuthManager = {
    init() {
        document.getElementById('loginPassword').addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.login();
        });
        document.getElementById('registerPasswordConfirm').addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.register();
        });
    },

    switchTab(tab) {
        document.querySelectorAll('.auth-tab').forEach(t => t.classList.remove('active'));
        document.querySelectorAll('.auth-form').forEach(f => f.classList.remove('active'));
        
        if (tab === 'login') {
            document.querySelectorAll('.auth-tab')[0].classList.add('active');
            document.getElementById('loginForm').classList.add('active');
        } else {
            document.querySelectorAll('.auth-tab')[1].classList.add('active');
            document.getElementById('registerForm').classList.add('active');
        }
    },

    login() {
        const username = document.getElementById('loginUsername').value.trim();
        const password = document.getElementById('loginPassword').value;
        const errorDiv = document.getElementById('loginError');
        
        if (!username || !password) {
            errorDiv.textContent = 'Por favor completa todos los campos';
            return;
        }

        WebSocketManager.send({
            type: 'login',
            username: username,
            password: password
        });
    },

    register() {
        const username = document.getElementById('registerUsername').value.trim();
        const password = document.getElementById('registerPassword').value;
        const confirmPassword = document.getElementById('registerPasswordConfirm').value;
        const errorDiv = document.getElementById('registerError');
        
        if (!username || !password || !confirmPassword) {
            errorDiv.textContent = 'Por favor completa todos los campos';
            return;
        }

        if (password !== confirmPassword) {
            errorDiv.textContent = 'Las contraseñas no coinciden';
            return;
        }

        WebSocketManager.send({
            type: 'register',
            username: username,
            password: password
        });
    },

    logout() {
        if (AppState.currentUser) {
            WebSocketManager.send({ type: 'logout' });
            location.reload();
        }
    }
};

// ==================== WEBSOCKET MANAGER ====================
const WebSocketManager = {
    ws: null,

    connect() {
        this.ws = new WebSocket(CONFIG.WS_URL);
        
        this.ws.onopen = () => {
            console.log('[WebSocket] Connected');
        };
        
        this.ws.onmessage = (event) => {
            const data = JSON.parse(event.data);
            MessageHandler.handleMessage(data);
        };
        
        this.ws.onerror = (error) => {
            console.error('[WebSocket] Error:', error);
        };
        
        this.ws.onclose = () => {
            console.log('[WebSocket] Disconnected');
        };
    },

    send(data) {
        if (this.ws && this.ws.readyState === WebSocket.OPEN) {
            this.ws.send(JSON.stringify(data));
        }
    }
};

// ==================== MESSAGE HANDLER ====================
const MessageHandler = {
    handleMessage(data) {
        const type = data.type;
        
        switch(type) {
            case 'registerResponse':
                this.handleRegisterResponse(data);
                break;
            case 'loginResponse':
                this.handleLoginResponse(data);
                break;
            case 'userList':
                AppState.contacts = data.users;
                UIManager.renderContacts();
                break;
            case 'userStatus':
                this.handleUserStatus(data);
                break;
            case 'privateMessage':
                this.handlePrivateMessage(data);
                break;
            case 'messageSent':
                this.handleMessageSent(data);
                break;
            case 'privateConversationHistory':
                this.handlePrivateConversationHistory(data);
                break;
            case 'conversationHistory':
                this.handleConversationHistory(data);
                break;
            case 'groupList':
                AppState.groups = data.groups;
                UIManager.renderGroups();
                break;
            case 'newGroup':
                if (!AppState.groups.find(g => g.id === data.id)) {
                    AppState.groups.push(data);
                }
                if (AppState.currentView === 'groups') {
                    UIManager.renderGroups();
                }
                break;
            case 'groupMessage':
                this.handleGroupMessage(data);
                break;
            case 'callRequest':
                VideoCallManager.handleCallRequest(data);
                break;
            case 'callOffer':
                VideoCallManager.handleCallOffer(data);
                break;
            case 'callAnswer':
                VideoCallManager.handleCallAnswer(data);
                break;
            case 'iceCandidate':
                VideoCallManager.handleIceCandidate(data);
                break;
            case 'callEnded':
                VideoCallManager.handleCallEnded(data);
                break;
            case 'callRejected':
                VideoCallManager.handleCallRejected(data);
                break;
            case 'profilePictureUpdated':
                this.handleProfilePictureUpdated(data);
                break;
            case 'profilePictureUpdate':
                this.handleProfilePictureUpdate(data);
                break;
            // Contact handlers
            case 'searchUserResponse':
                ContactManager.handleSearchUserResponse(data);
                break;
            case 'addContactResponse':
                ContactManager.handleAddContactResponse(data);
                break;
            case 'removeContactResponse':
                ContactManager.handleRemoveContactResponse(data);
                break;
            case 'contactRemoved':
                ContactManager.handleContactRemoved(data);
                break;
            // Friend Request handlers
            case 'friendRequestResponse':
                ContactManager.handleFriendRequestResponse(data);
                break;
            case 'newFriendRequest':
                ContactManager.handleNewFriendRequest(data);
                break;
            case 'friendRequestAccepted':
                ContactManager.handleFriendRequestAccepted(data);
                break;
            case 'acceptRequestResponse':
                ContactManager.handleAcceptRequestResponse(data);
                break;
            case 'rejectRequestResponse':
                ContactManager.handleRejectRequestResponse(data);
                break;
            case 'pendingRequests':
                ContactManager.handlePendingRequests(data);
                break;
            case 'sentRequests':
                ContactManager.handleSentRequests(data);
                break;
            default:
                console.log('[Message] Unknown type:', type);
        }
    },

    handleRegisterResponse(data) {
        const errorDiv = document.getElementById('registerError');
        if (data.success) {
            errorDiv.textContent = 'Cuenta creada exitosamente. Inicia sesión.';
            errorDiv.style.color = '#28a745';
            setTimeout(() => AuthManager.switchTab('login'), 2000);
        } else {
            errorDiv.textContent = 'Error: ' + data.error;
            errorDiv.style.color = '#dc3545';
        }
    },

    handleLoginResponse(data) {
        if (data.success) {
            AppState.currentUser = data.username;
            AppState.currentUserProfilePicture = data.profilePicture || null;
            UIManager.showApp();
            ProfileManager.initAvatar(data.username, data.profilePicture);
            ChatManager.requestUserList();
            
            // Cargar solicitudes pendientes
            ContactManager.loadPendingRequests();
            
            // Pedir permiso para notificaciones
            if (Notification.permission === 'default') {
                Notification.requestPermission();
            }
        } else {
            document.getElementById('loginError').textContent = 'Error login: ' + data.error;
        }
    },

    handleProfilePictureUpdated(data) {
        if (data.success) {
            console.log('[Profile] Foto de perfil actualizada');
        }
    },

    handleProfilePictureUpdate(data) {
        // Otro usuario actualizó su foto de perfil
        const contact = AppState.contacts.find(c => c.username === data.username);
        if (contact) {
            contact.profilePicture = data.profilePicture;
            UIManager.renderContacts();
            
            // Actualizar también en el header del chat si es el chat activo
            if (AppState.activeChat === data.username && AppState.activeChatType === 'private') {
                UIManager.updateChatHeaderAvatar(data.username, data.profilePicture);
            }
        }
    },

    handleUserStatus(data) {
        const contact = AppState.contacts.find(c => c.username === data.username);
        if (contact) {
            contact.online = data.online;
            // Si se desconecta, usar el lastSeen del servidor
            if (!data.online && data.lastSeen) {
                contact.lastSeen = data.lastSeen;
            }
            UIManager.renderContacts();
            
            // Actualizar header del chat si es el chat activo
            if (AppState.activeChat === data.username && AppState.activeChatType === 'private') {
                const statusEl = document.getElementById('chatHeaderStatus');
                if (statusEl) {
                    if (data.online) {
                        statusEl.textContent = 'En línea';
                    } else {
                        statusEl.textContent = 'Últ. vez ' + UIManager.formatLastSeen(contact.lastSeen);
                    }
                }
            }
        }
    },

    handlePrivateMessage(data) {
        ChatManager.receiveMessage(data.from, data.content, data.contentType, data.timestamp);
    },

    handleMessageSent(data) {
        ChatManager.addMessageToChat(data.to, AppState.currentUser, data.content, data.contentType, data.timestamp, true);
    },

    handlePrivateConversationHistory(data) {
        ChatManager.loadPrivateConversationHistory(data);
    },

    handleConversationHistory(data) {
        ChatManager.loadConversationHistory(data);
    },

    handleGroupMessage(data) {
        ChatManager.receiveGroupMessage(data.groupId, data.from, data.content, data.contentType, data.timestamp);
    }
};

// ==================== CHAT MANAGER ====================
const ChatManager = {
    requestUserList() {
        WebSocketManager.send({ type: 'getUsers' });
    },

    requestGroups() {
        WebSocketManager.send({ type: 'getGroups' });
    },

    sendMessage() {
        const input = document.getElementById('messageInput');
        const content = input.value.trim();
        
        if (!content || !AppState.activeChat) return;
        
        if (AppState.activeChatType === 'group') {
            WebSocketManager.send({
                type: 'groupMessage',
                groupId: AppState.activeChat,
                content: content,
                contentType: 'text'
            });
        } else {
            WebSocketManager.send({
                type: 'privateMessage',
                to: AppState.activeChat,
                content: content,
                contentType: 'text'
            });
        }
        
        input.value = '';
    },


    receiveMessage(from, content, contentType, timestamp) {
        this.addMessageToChat(from, from, content, contentType, timestamp, false);
    },

    addMessageToChat(chatUser, from, content, contentType, timestamp, isSent) {
        if (!AppState.conversations[chatUser]) {
            AppState.conversations[chatUser] = [];
        }
        
        AppState.conversations[chatUser].push({ from, content, contentType, timestamp });
        
        if (AppState.activeChat === chatUser) {
            UIManager.displayMessage(from, content, contentType, timestamp, isSent);
        }
    },

    loadPrivateConversationHistory(data) {
        const otherUser = data.with;

        if (data.messages && data.messages.length > 0) {
            AppState.conversations[otherUser] = [];
            data.messages.forEach(msg => {
                AppState.conversations[otherUser].push({
                    from: msg.from,
                    content: msg.content,
                    contentType: msg.contentType,
                    timestamp: msg.timestamp
                });
            });
        }
        
        if (AppState.activeChat === otherUser && AppState.activeChatType === 'private') {
            const container = document.getElementById('messagesContainer');
            if (container) {
                container.innerHTML = '';
                const messagesToRender = (data.messages && data.messages.length > 0)
                    ? data.messages
                    : (AppState.conversations[otherUser] || []);

                messagesToRender.forEach(msg => {
                    UIManager.displayMessage(msg.from, msg.content, msg.contentType, msg.timestamp, msg.from === AppState.currentUser);
                });
            }
        }
    },

    loadConversationHistory(data) {
        if (data.conversations) {
            for (const [otherUser, messages] of Object.entries(data.conversations)) {
                if (!AppState.conversations[otherUser]) {
                    AppState.conversations[otherUser] = [];
                }
                
                messages.forEach(msg => {
                    AppState.conversations[otherUser].push({
                        from: msg.from,
                        content: msg.content,
                        contentType: msg.contentType,
                        timestamp: msg.timestamp
                    });
                });
            }
        }
        
        if (data.groups) {
            for (const [groupId, messages] of Object.entries(data.groups)) {
                if (!AppState.conversations[groupId]) {
                    AppState.conversations[groupId] = [];
                }
                
                messages.forEach(msg => {
                    AppState.conversations[groupId].push({
                        from: msg.from,
                        content: msg.content,
                        contentType: msg.contentType,
                        timestamp: msg.timestamp
                    });
                });
            }
        }
    },

    receiveGroupMessage(groupId, from, content, contentType, timestamp) {
        if (!AppState.conversations[groupId]) {
            AppState.conversations[groupId] = [];
        }
        
        AppState.conversations[groupId].push({ from, content, contentType, timestamp });
        
        if (AppState.activeChat === groupId && AppState.activeChatType === 'group') {
            const isSent = from === AppState.currentUser;
            UIManager.displayGroupMessage(from, content, contentType, timestamp, isSent);
        }
    },

    openChat(username) {
        AppState.activeChat = username;
        AppState.activeChatType = 'private';
        UIManager.renderContacts();
        
        const contact = AppState.contacts.find(c => c.username === username);
        const avatarContent = contact?.profilePicture 
            ? `<img src="${contact.profilePicture}" style="width:100%;height:100%;object-fit:cover;border-radius:50%;">`
            : username[0].toUpperCase();
        
        // Determinar estado del contacto
        let statusText = 'Desconectado';
        if (contact?.online) {
            statusText = 'En línea';
        } else if (contact?.lastSeen) {
            statusText = 'Últ. vez ' + UIManager.formatLastSeen(contact.lastSeen);
        }
        
        const chatArea = document.getElementById('chatArea');
        chatArea.innerHTML = `
            <div class="chat-header">
                <button class="input-btn mobile-back-btn" onclick="toggleMobileSidebar(false)" title="Volver"><svg viewBox="0 0 24 24" width="24" height="24" fill="currentColor"><path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"/></svg></button>
                <div class="chat-header-avatar">${avatarContent}</div>
                <div class="chat-header-info">
                    <h3>${username}</h3>
                    <p id="chatHeaderStatus">${statusText}</p>
                </div>
                <div class="chat-header-actions">
                    <button class="input-btn" onclick="VideoCallManager.initiateCall()" title="Videollamada">${CONFIG.ICONS.CALL}</button>
                    <button class="input-btn delete-contact-btn" onclick="ContactManager.removeContact('${username}')" title="Eliminar contacto">
                        <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor">
                            <path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/>
                        </svg>
                    </button>
                </div>
            </div>
            <div class="messages-container" id="messagesContainer"></div>
            <div class="input-area">
                <button class="input-btn" onclick="UIManager.toggleEmojiPicker()" title="Emojis">${CONFIG.ICONS.EMOJI}</button>
                <button class="input-btn" onclick="MediaManager.selectImage()" title="Enviar imagen">${CONFIG.ICONS.IMAGE}</button>
                <button class="input-btn" onclick="MediaManager.selectVideo()" title="Enviar video">${CONFIG.ICONS.VIDEO}</button>
                <input type="text" id="messageInput" placeholder="Escribe un mensaje..." onkeypress="if(event.key==='Enter') ChatManager.sendMessage()" />
                <button id="sendBtn" onclick="ChatManager.sendMessage()">${CONFIG.ICONS.SEND}</button>
            </div>
        `;

        if (AppState.conversations[username]) {
            AppState.conversations[username].forEach(msg => {
                UIManager.displayMessage(msg.from, msg.content, msg.contentType, msg.timestamp, msg.from === AppState.currentUser);
            });
        }
        
        WebSocketManager.send({
            type: 'loadPrivateConversation',
            with: username
        });
        
        UIManager.scrollToBottom();
    },

    openGroupChat(groupId) {
        AppState.activeChat = groupId;
        AppState.activeChatType = 'group';
        UIManager.renderGroups();
        
        const group = AppState.groups.find(g => g.id === groupId);
        if (!group) return;
        
        const chatArea = document.getElementById('chatArea');
        chatArea.innerHTML = `
            <div class="chat-header">
                <button class="input-btn mobile-back-btn" onclick="toggleMobileSidebar(false)" title="Volver"><svg viewBox="0 0 24 24" width="24" height="24" fill="currentColor"><path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"/></svg></button>
                <div class="chat-header-avatar" style="background: linear-gradient(135deg, #667eea, #764ba2);">${group.name[0].toUpperCase()}</div>
                <div class="chat-header-info">
                    <h3>${group.name}</h3>
                    <p>${group.members.length} miembros</p>
                </div>
            </div>
            <div class="messages-container" id="messagesContainer"></div>
            <div class="input-area">
                <button class="input-btn" onclick="UIManager.toggleEmojiPicker()" title="Emojis">${CONFIG.ICONS.EMOJI}</button>
                <button class="input-btn" onclick="MediaManager.selectImage()" title="Enviar imagen">${CONFIG.ICONS.IMAGE}</button>
                <button class="input-btn" onclick="MediaManager.selectVideo()" title="Enviar video">${CONFIG.ICONS.VIDEO}</button>
                <input type="text" id="messageInput" placeholder="Escribe un mensaje..." onkeypress="if(event.key==='Enter') ChatManager.sendMessage()" />
                <button id="sendBtn" onclick="ChatManager.sendMessage()">${CONFIG.ICONS.SEND}</button>
            </div>
        `;
        
        if (AppState.conversations[groupId]) {
            AppState.conversations[groupId].forEach(msg => {
                const isSent = msg.from === AppState.currentUser;
                UIManager.displayGroupMessage(msg.from, msg.content, msg.contentType, msg.timestamp, isSent);
            });
        }
        
        UIManager.scrollToBottom();
    }
};

// ==================== UI MANAGER ====================
const UIManager = {
    showAuth() {
        document.getElementById('authScreen').style.display = 'flex';
        document.getElementById('appScreen').style.display = 'none';
    },

    showApp() {
        document.getElementById('authScreen').style.display = 'none';
        document.getElementById('appScreen').style.display = 'flex';
        document.getElementById('currentUsername').textContent = AppState.currentUser;
        
        ChatManager.requestUserList();
        ChatManager.requestGroups();
        WebSocketManager.send({ type: 'loadConversationHistory' });
    },

    renderContacts() {
        const list = document.getElementById('contactList');
        list.innerHTML = '';
        
        // Si no hay contactos, mostrar mensaje
        if (AppState.contacts.length === 0) {
            list.innerHTML = `
                <div style="padding: 40px 20px; text-align: center; color: #666;">
                    <svg viewBox="0 0 24 24" width="60" height="60" fill="#ccc" style="margin-bottom: 15px;">
                        <path d="M15 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm-9-2V7H4v3H1v2h3v3h2v-3h3v-2H6zm9 4c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/>
                    </svg>
                    <h3 style="margin: 0 0 10px 0; color: #333;">No tienes contactos</h3>
                    <p style="margin: 0 0 20px 0; font-size: 0.9rem;">Agrega contactos para empezar a chatear</p>
                    <button onclick="ContactManager.openAddContactModal()" style="padding: 12px 24px; background: #25D366; color: white; border: none; border-radius: 25px; cursor: pointer; font-weight: bold;">
                        + Agregar Contacto
                    </button>
                </div>
            `;
            return;
        }
        
        AppState.contacts.forEach(contact => {
            const item = document.createElement('div');
            item.className = 'contact-item';
            if (AppState.activeChat === contact.username) item.classList.add('active');
            
            const avatarContent = contact.profilePicture 
                ? `<img src="${contact.profilePicture}" style="width:100%;height:100%;object-fit:cover;border-radius:50%;">`
                : contact.username[0].toUpperCase();
            
            // Determinar el estado del contacto
            let statusText = 'Desconectado';
            if (contact.online) {
                statusText = 'En línea';
            } else if (contact.lastSeen) {
                statusText = 'Últ. vez ' + this.formatLastSeen(contact.lastSeen);
            }
            
            item.innerHTML = `
                <div class="contact-avatar">
                    ${avatarContent}
                    ${contact.online ? '<div class="online-indicator"></div>' : ''}
                </div>
                <div class="contact-info">
                    <div class="contact-name">${contact.username}</div>
                    <div class="contact-status">${statusText}</div>
                </div>
            `;
            
            item.onclick = () => ChatManager.openChat(contact.username);
            list.appendChild(item);
        });
    },

    // Formatear última conexión
    formatLastSeen(timestamp) {
        const now = Date.now();
        const diff = now - timestamp;
        const seconds = Math.floor(diff / 1000);
        const minutes = Math.floor(seconds / 60);
        const hours = Math.floor(minutes / 60);
        const days = Math.floor(hours / 24);
        
        if (seconds < 60) {
            return 'hace un momento';
        } else if (minutes < 60) {
            return `hace ${minutes} min`;
        } else if (hours < 24) {
            return `hace ${hours} h`;
        } else if (days === 1) {
            return 'ayer';
        } else if (days < 7) {
            return `hace ${days} días`;
        } else {
            const date = new Date(timestamp);
            return date.toLocaleDateString('es-ES', { day: 'numeric', month: 'short' });
        }
    },

    updateChatHeaderAvatar(username, profilePicture) {
        const headerAvatar = document.querySelector('.chat-header-avatar');
        if (headerAvatar) {
            if (profilePicture) {
                headerAvatar.innerHTML = `<img src="${profilePicture}" style="width:100%;height:100%;object-fit:cover;border-radius:50%;">`;
            } else {
                headerAvatar.textContent = username[0].toUpperCase();
            }
        }
    },

    renderGroups() {
        const list = document.getElementById('groupList');
        list.innerHTML = `
            <div style="padding: 15px; border-bottom: 1px solid #dadada;">
                <button onclick="GroupManager.showCreateGroupModal()" style="width: 100%; padding: 12px; background: #128C7E; color: white; border: none; border-radius: 8px; cursor: pointer; font-weight: bold;">+ Crear Grupo</button>
            </div>
        `;
        
        AppState.groups.forEach(group => {
            const item = document.createElement('div');
            item.className = 'contact-item';
            if (AppState.activeChat === group.id && AppState.activeChatType === 'group') item.classList.add('active');
            
            item.innerHTML = `
                <div class="contact-avatar" style="background: linear-gradient(135deg, #667eea, #764ba2);">
                    ${group.name[0].toUpperCase()}
                </div>
                <div class="contact-info">
                    <div class="contact-name">${group.name}</div>
                    <div class="contact-status">${group.members.length} miembros</div>
                </div>
            `;
            
            item.onclick = () => ChatManager.openGroupChat(group.id);
            list.appendChild(item);
        });
    },

    formatTimestamp(timestamp) {
        if (!timestamp) return '';

        if (timestamp instanceof Date) {
            return `${String(timestamp.getHours()).padStart(2, '0')}:${String(timestamp.getMinutes()).padStart(2, '0')}`;
        }

        if (typeof timestamp === 'string') {
            const timeMatch = timestamp.match(/(\d{2}):(\d{2})/);
            if (timeMatch) {
                return `${timeMatch[1]}:${timeMatch[2]}`;
            }
        }

        return String(timestamp);
    },

    displayMessage(from, content, contentType, timestamp, isSent) {
        const container = document.getElementById('messagesContainer');
        if (!container) return;
        
        const msg = document.createElement('div');
        msg.className = `message ${isSent ? 'sent' : 'received'}`;
        
        const bubble = document.createElement('div');
        bubble.className = 'message-bubble';
        
        if (contentType === 'image') {
            const img = document.createElement('img');
            img.src = content;
            img.className = 'message-image';
            img.onclick = () => this.showImageModal(content);
            bubble.appendChild(img);
        } else if (contentType === 'video') {
            const video = document.createElement('video');
            video.src = content;
            video.className = 'message-video';
            video.controls = true;
            video.preload = 'metadata';
            video.setAttribute('playsinline', '');
            video.setAttribute('webkit-playsinline', '');
            video.setAttribute('x5-playsinline', '');
            bubble.appendChild(video);
        } else {
            const textDiv = document.createElement('div');
            textDiv.className = 'message-content';
            textDiv.textContent = content;
            bubble.appendChild(textDiv);
        }
        
        const timeDiv = document.createElement('div');
        timeDiv.className = 'message-time';
        timeDiv.textContent = this.formatTimestamp(timestamp);
        bubble.appendChild(timeDiv);
        
        msg.appendChild(bubble);
        container.appendChild(msg);
        this.scrollToBottom();
    },

    displayGroupMessage(from, content, contentType, timestamp, isSent) {
        const container = document.getElementById('messagesContainer');
        if (!container) return;
        
        const msg = document.createElement('div');
        msg.className = `message ${isSent ? 'sent' : 'received'}`;
        
        const bubble = document.createElement('div');
        bubble.className = 'message-bubble';
        
        const senderDiv = document.createElement('div');
        senderDiv.style.fontSize = '0.8rem';
        senderDiv.style.fontWeight = 'bold';
        senderDiv.style.marginBottom = '4px';
        senderDiv.style.color = '#128C7E';
        senderDiv.textContent = isSent ? 'Tú' : from;
        bubble.appendChild(senderDiv);
        
        if (contentType === 'image') {
            const img = document.createElement('img');
            img.src = content;
            img.className = 'message-image';
            img.onclick = () => this.showImageModal(content);
            bubble.appendChild(img);
        } else if (contentType === 'video') {
            const video = document.createElement('video');
            video.src = content;
            video.className = 'message-video';
            video.controls = true;
            video.preload = 'metadata';
            video.setAttribute('playsinline', '');
            video.setAttribute('webkit-playsinline', '');
            video.setAttribute('x5-playsinline', '');
            bubble.appendChild(video);
        } else {
            const textDiv = document.createElement('div');
            textDiv.className = 'message-content';
            textDiv.textContent = content;
            bubble.appendChild(textDiv);
        }
        
        const timeDiv = document.createElement('div');
        timeDiv.className = 'message-time';
        timeDiv.textContent = this.formatTimestamp(timestamp);
        bubble.appendChild(timeDiv);
        
        msg.appendChild(bubble);
        container.appendChild(msg);
        this.scrollToBottom();
    },

    scrollToBottom() {
        const container = document.getElementById('messagesContainer');
        if (container) {
            setTimeout(() => {
                container.scrollTop = container.scrollHeight;
            }, 0);
        }
    },

    showImageModal(src) {
        document.getElementById('modalImage').src = src;
        document.getElementById('imageModal').classList.add('show');
    },

    closeImageModal() {
        document.getElementById('imageModal').classList.remove('show');
    },

    toggleEmojiPicker() {
        document.getElementById('emojiPicker').classList.toggle('show');
    },

    insertEmoji(emoji) {
        const input = document.getElementById('messageInput');
        input.value += emoji;
        input.focus();
        this.toggleEmojiPicker();
    },

    initEmojiPicker() {
        const grid = document.getElementById('emojiGrid');
        CONFIG.EMOJIS.forEach(emoji => {
            const item = document.createElement('div');
            item.className = 'emoji-item';
            item.textContent = emoji;
            item.onclick = () => this.insertEmoji(emoji);
            grid.appendChild(item);
        });
    },

    switchView(view) {
        AppState.currentView = view;
        
        document.getElementById('contactsTab').classList.toggle('active', view === 'contacts');
        document.getElementById('groupsTab').classList.toggle('active', view === 'groups');
        
        document.getElementById('contactList').classList.toggle('hidden', view === 'groups');
        document.getElementById('groupList').classList.toggle('hidden', view === 'contacts');
        
        if (view === 'contacts') {
            this.renderContacts();
        } else {
            this.renderGroups();
        }
    },

    filterContacts() {
        const query = document.getElementById('searchInput').value.toLowerCase();
        const items = document.querySelectorAll('.contact-item');
        
        items.forEach(item => {
            const name = item.querySelector('.contact-name').textContent.toLowerCase();
            item.style.display = name.includes(query) ? 'flex' : 'none';
        });
    }
};

// ==================== MEDIA MANAGER (CLOUDINARY) ====================
const MediaManager = {
    // Abre el selector de archivos para imagen
    selectImage() {
        const input = document.createElement('input');
        input.type = 'file';
        input.accept = 'image/*';
        input.onchange = (e) => this.handleFileSelect(e, 'image');
        input.click();
    },

    // Abre el selector de archivos para video
    selectVideo() {
        const input = document.createElement('input');
        input.type = 'file';
        input.accept = 'video/*';
        input.onchange = (e) => this.handleFileSelect(e, 'video');
        input.click();
    },

    // Maneja la selección de archivo
    async handleFileSelect(event, type) {
        const file = event.target.files[0];
        if (!file) return;

        // Validar tamaño (máx 100MB para video, 10MB para imagen)
        const maxSize = type === 'video' ? 100 * 1024 * 1024 : 10 * 1024 * 1024;
        if (file.size > maxSize) {
            alert(`El archivo es muy grande. Máximo: ${type === 'video' ? '100MB' : '10MB'}`);
            return;
        }

        // Mostrar indicador de carga
        this.showUploadProgress();

        try {
            const url = await this.uploadToCloudinary(file);
            this.hideUploadProgress();
            this.sendMediaMessage(url, type);
        } catch (error) {
            this.hideUploadProgress();
            console.error('Error subiendo archivo:', error);
            alert('Error al subir el archivo. Intenta de nuevo.');
        }
    },

    // Sube archivo a Cloudinary
    async uploadToCloudinary(file) {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('upload_preset', CONFIG.CLOUDINARY.UPLOAD_PRESET);

        const response = await fetch(CONFIG.CLOUDINARY.UPLOAD_URL, {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            throw new Error('Error en la subida');
        }

        const data = await response.json();
        return data.secure_url;
    },

    // Envía mensaje con media
    sendMediaMessage(url, type) {
        if (!AppState.activeChat) return;

        if (AppState.activeChatType === 'group') {
            WebSocketManager.send({
                type: 'groupMessage',
                groupId: AppState.activeChat,
                content: url,
                contentType: type
            });
        } else {
            WebSocketManager.send({
                type: 'privateMessage',
                to: AppState.activeChat,
                content: url,
                contentType: type
            });
        }
    },

    // Muestra indicador de progreso
    showUploadProgress() {
        let overlay = document.getElementById('uploadOverlay');
        if (!overlay) {
            overlay = document.createElement('div');
            overlay.id = 'uploadOverlay';
            overlay.innerHTML = `
                <div class="upload-spinner"></div>
                <p>Subiendo archivo...</p>
            `;
            overlay.style.cssText = `
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.7);
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                z-index: 9999;
                color: white;
                font-size: 18px;
            `;
            document.body.appendChild(overlay);
        }
        overlay.style.display = 'flex';
    },

    // Oculta indicador de progreso
    hideUploadProgress() {
        const overlay = document.getElementById('uploadOverlay');
        if (overlay) {
            overlay.style.display = 'none';
        }
    }
};

// ==================== CONTACT MANAGER ====================
const ContactManager = {
    foundUser: null,
    pendingRequests: [],
    sentRequests: [],
    activeRequestsTab: 'received',

    openAddContactModal() {
        document.getElementById('addContactModal').classList.add('active');
        document.getElementById('searchContactInput').value = '';
        document.getElementById('searchContactInput').focus();
        this.resetSearchResult();
    },

    closeAddContactModal() {
        document.getElementById('addContactModal').classList.remove('active');
        this.resetSearchResult();
    },

    resetSearchResult() {
        document.getElementById('searchResult').classList.remove('active', 'success');
        document.getElementById('searchError').style.display = 'none';
        document.getElementById('addContactBtn').style.display = 'none';
        this.foundUser = null;
    },

    searchUser() {
        const username = document.getElementById('searchContactInput').value.trim();
        
        if (!username) {
            this.showError('Ingresa un nombre de usuario');
            return;
        }

        document.getElementById('searchContactBtn').disabled = true;
        document.getElementById('searchContactBtn').textContent = 'Buscando...';
        
        WebSocketManager.send({
            type: 'searchUser',
            username: username
        });
    },

    handleSearchUserResponse(data) {
        document.getElementById('searchContactBtn').disabled = false;
        document.getElementById('searchContactBtn').textContent = 'Buscar';

        if (data.success) {
            this.foundUser = data.user;
            this.showFoundUser(data.user);
        } else {
            this.showError(data.error);
        }
    },

    showFoundUser(user) {
        document.getElementById('searchError').style.display = 'none';
        
        const resultDiv = document.getElementById('searchResult');
        const avatarDiv = document.getElementById('searchResultAvatar');
        const usernameEl = document.getElementById('searchResultUsername');
        const statusEl = document.getElementById('searchResultStatus');

        // Mostrar avatar
        if (user.profilePicture) {
            avatarDiv.innerHTML = `<img src="${user.profilePicture}" alt="${user.username}">`;
        } else {
            avatarDiv.innerHTML = `<span>${user.username[0].toUpperCase()}</span>`;
        }

        usernameEl.textContent = user.username;
        statusEl.textContent = user.online ? 'En línea' : 'Desconectado';
        statusEl.style.color = user.online ? '#25D366' : '#666';

        resultDiv.classList.add('active', 'success');
        document.getElementById('addContactBtn').style.display = 'block';
        document.getElementById('addContactBtn').textContent = 'Enviar Solicitud';
    },

    showError(message) {
        document.getElementById('searchResult').classList.remove('active', 'success');
        const errorDiv = document.getElementById('searchError');
        document.getElementById('searchErrorText').textContent = message;
        errorDiv.style.display = 'block';
        document.getElementById('addContactBtn').style.display = 'none';
    },

    // Enviar solicitud de amistad en vez de agregar directamente
    addFoundContact() {
        if (!this.foundUser) return;

        document.getElementById('addContactBtn').disabled = true;
        document.getElementById('addContactBtn').textContent = 'Enviando...';

        WebSocketManager.send({
            type: 'sendFriendRequest',
            username: this.foundUser.username
        });
    },

    // Handler para respuesta de envío de solicitud
    handleFriendRequestResponse(data) {
        document.getElementById('addContactBtn').disabled = false;
        document.getElementById('addContactBtn').textContent = 'Enviar Solicitud';

        if (data.success) {
            this.closeAddContactModal();
            this.showNotification('Solicitud enviada', `Se envió solicitud de amistad a ${this.foundUser.username}`);
        } else {
            this.showError(data.error);
        }
    },

    // Handler para nueva solicitud recibida (tiempo real)
    handleNewFriendRequest(data) {
        const request = data.request;
        this.pendingRequests.push(request);
        this.updateRequestsBadge();
        this.showNotification('Nueva solicitud', `${request.fromUsername} quiere ser tu contacto`);
        
        // Si el modal de solicitudes está abierto, actualizar
        if (document.getElementById('requestsModal').classList.contains('active')) {
            this.renderPendingRequests();
        }
    },

    // Handler cuando alguien acepta nuestra solicitud
    handleFriendRequestAccepted(data) {
        const contact = data.contact;
        AppState.contacts.push(contact);
        UIManager.renderContacts();
        this.showNotification('Solicitud aceptada', `${contact.username} aceptó tu solicitud`);
    },

    // Handler para respuesta de aceptar solicitud
    handleAcceptRequestResponse(data) {
        if (data.success) {
            // Agregar nuevo contacto
            AppState.contacts.push(data.contact);
            UIManager.renderContacts();
            
            // Remover de pendientes
            this.pendingRequests = this.pendingRequests.filter(r => r.fromUsername !== data.contact.username);
            this.updateRequestsBadge();
            this.renderPendingRequests();
            
            this.showNotification('Contacto agregado', `Ahora ${data.contact.username} es tu contacto`);
        }
    },

    // Handler para respuesta de rechazar solicitud
    handleRejectRequestResponse(data) {
        if (data.success) {
            this.pendingRequests = this.pendingRequests.filter(r => r.id !== data.requestId);
            this.updateRequestsBadge();
            this.renderPendingRequests();
        }
    },

    // Cargar solicitudes pendientes desde el servidor
    loadPendingRequests() {
        WebSocketManager.send({ type: 'getPendingRequests' });
    },

    // Handler para lista de solicitudes pendientes
    handlePendingRequests(data) {
        this.pendingRequests = data.requests || [];
        this.updateRequestsBadge();
        this.renderPendingRequests();
    },

    // Actualizar badge de solicitudes
    updateRequestsBadge() {
        const badge = document.getElementById('requestsBadge');
        const count = this.pendingRequests.length;
        if (badge) {
            badge.textContent = count;
            badge.style.display = count > 0 ? 'flex' : 'none';
        }
    },

    // Handler para lista de solicitudes enviadas
    handleSentRequests(data) {
        this.sentRequests = data.requests || [];
        if (this.activeRequestsTab === 'sent') {
            this.renderSentRequests();
        }
    },

    // Abrir modal de solicitudes
    openRequestsModal() {
        document.getElementById('requestsModal').classList.add('active');
        this.activeRequestsTab = 'received';
        this.updateTabsUI();
        this.loadPendingRequests();
        this.loadSentRequests();
    },

    closeRequestsModal() {
        document.getElementById('requestsModal').classList.remove('active');
    },

    // Mostrar pestaña de solicitudes recibidas
    showReceivedRequests() {
        this.activeRequestsTab = 'received';
        this.updateTabsUI();
        this.renderPendingRequests();
    },

    // Mostrar pestaña de solicitudes enviadas
    showSentRequests() {
        this.activeRequestsTab = 'sent';
        this.updateTabsUI();
        this.renderSentRequests();
    },

    // Actualizar UI de tabs
    updateTabsUI() {
        const receivedTab = document.getElementById('receivedTab');
        const sentTab = document.getElementById('sentTab');
        if (receivedTab && sentTab) {
            receivedTab.classList.toggle('active', this.activeRequestsTab === 'received');
            sentTab.classList.toggle('active', this.activeRequestsTab === 'sent');
        }
    },

    // Cargar solicitudes enviadas
    loadSentRequests() {
        WebSocketManager.send({
            type: 'getSentRequests'
        });
    },

    // Renderizar solicitudes enviadas
    renderSentRequests() {
        const container = document.getElementById('pendingRequestsList');
        if (!container) return;

        if (this.sentRequests.length === 0) {
            container.innerHTML = `
                <div style="padding: 40px 20px; text-align: center; color: #666;">
                    <svg viewBox="0 0 24 24" width="50" height="50" fill="#ccc" style="margin-bottom: 10px;">
                        <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm-1-13h2v6h-2zm0 8h2v2h-2z"/>
                    </svg>
                    <p>No has enviado solicitudes</p>
                </div>
            `;
            return;
        }

        container.innerHTML = this.sentRequests.map(request => {
            const avatarContent = request.profilePicture 
                ? `<img src="${request.profilePicture}" style="width:100%;height:100%;object-fit:cover;border-radius:50%;">`
                : request.toUsername[0].toUpperCase();
            
            return `
                <div class="request-item">
                    <div class="request-avatar">${avatarContent}</div>
                    <div class="request-info">
                        <h4>${request.toUsername}</h4>
                        <p style="color: #FFA000;">Pendiente</p>
                    </div>
                    <div class="request-actions">
                        <button class="reject-btn" onclick="ContactManager.cancelRequest('${request.id}')">Cancelar</button>
                    </div>
                </div>
            `;
        }).join('');
    },

    // Cancelar solicitud enviada
    cancelRequest(requestId) {
        WebSocketManager.send({
            type: 'rejectFriendRequest',
            requestId: requestId
        });
    },

    // Renderizar lista de solicitudes pendientes
    renderPendingRequests() {
        const container = document.getElementById('pendingRequestsList');
        if (!container) return;

        if (this.pendingRequests.length === 0) {
            container.innerHTML = `
                <div style="padding: 40px 20px; text-align: center; color: #666;">
                    <svg viewBox="0 0 24 24" width="50" height="50" fill="#ccc" style="margin-bottom: 10px;">
                        <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm-1-13h2v6h-2zm0 8h2v2h-2z"/>
                    </svg>
                    <p>No tienes solicitudes pendientes</p>
                </div>
            `;
            return;
        }

        container.innerHTML = this.pendingRequests.map(request => {
            const avatarContent = request.profilePicture 
                ? `<img src="${request.profilePicture}" style="width:100%;height:100%;object-fit:cover;border-radius:50%;">`
                : request.fromUsername[0].toUpperCase();
            
            return `
                <div class="request-item">
                    <div class="request-avatar">${avatarContent}</div>
                    <div class="request-info">
                        <h4>${request.fromUsername}</h4>
                        <p>Quiere ser tu contacto</p>
                    </div>
                    <div class="request-actions">
                        <button class="accept-btn" onclick="ContactManager.acceptRequest('${request.id}')">Aceptar</button>
                        <button class="reject-btn" onclick="ContactManager.rejectRequest('${request.id}')">Rechazar</button>
                    </div>
                </div>
            `;
        }).join('');
    },

    // Aceptar solicitud
    acceptRequest(requestId) {
        WebSocketManager.send({
            type: 'acceptFriendRequest',
            requestId: requestId
        });
    },

    // Rechazar solicitud
    rejectRequest(requestId) {
        WebSocketManager.send({
            type: 'rejectFriendRequest',
            requestId: requestId
        });
    },

    // Mostrar notificación
    showNotification(title, message) {
        // Crear notificación visual
        const notification = document.createElement('div');
        notification.className = 'toast-notification';
        notification.innerHTML = `
            <strong>${title}</strong>
            <p>${message}</p>
        `;
        document.body.appendChild(notification);

        // Animar entrada
        setTimeout(() => notification.classList.add('show'), 10);

        // Remover después de 4 segundos
        setTimeout(() => {
            notification.classList.remove('show');
            setTimeout(() => notification.remove(), 300);
        }, 4000);

        // También notificación del navegador si está permitido
        if (Notification.permission === 'granted') {
            new Notification(title, { body: message });
        }
    },

    handleAddContactResponse(data) {
        // Compatibilidad con respuesta antigua (no se usa ahora)
        if (data.success) {
            this.closeAddContactModal();
        } else {
            this.showError(data.error);
        }
    },

    handleRemoveContactResponse(data) {
        if (data.success) {
            // Eliminar de la lista de contactos
            AppState.contacts = AppState.contacts.filter(c => c.username !== data.username);

            // Eliminar conversación local
            delete AppState.conversations[data.username];

            // Si el chat activo era con ese contacto, cerrarlo
            if (AppState.activeChat === data.username && AppState.activeChatType === 'private') {
                AppState.activeChat = null;
                document.getElementById('chatArea').innerHTML = `
                    <div class="no-chat-selected">
                        <div class="no-chat-icon">${CONFIG.ICONS.CHAT}</div>
                        <h2>Selecciona un chat</h2>
                        <p>Elige un contacto o grupo para comenzar a chatear</p>
                    </div>
                `;
            }

            UIManager.renderContacts();
            this.showNotification('Contacto eliminado', `${data.username} ha sido eliminado de tus contactos`);
        }
    },

    // Cuando otro usuario nos elimina como contacto
    handleContactRemoved(data) {
        // Eliminar de la lista de contactos
        AppState.contacts = AppState.contacts.filter(c => c.username !== data.username);

        // Eliminar conversación local
        delete AppState.conversations[data.username];

        // Si el chat activo era con ese contacto, cerrarlo
        if (AppState.activeChat === data.username && AppState.activeChatType === 'private') {
            AppState.activeChat = null;
            document.getElementById('chatArea').innerHTML = `
                <div class="no-chat-selected">
                    <div class="no-chat-icon">${CONFIG.ICONS.CHAT}</div>
                    <h2>Selecciona un chat</h2>
                    <p>Elige un contacto o grupo para comenzar a chatear</p>
                </div>
            `;
        }

        UIManager.renderContacts();
        this.showNotification('Contacto eliminado', `${data.username} te ha eliminado de sus contactos`);
    },

    removeContact(username) {
        if (confirm(`¿Eliminar a ${username} de tus contactos?`)) {
            WebSocketManager.send({
                type: 'removeContact',
                username: username
            });
        }
    }
};

// ==================== PROFILE MANAGER ====================
const ProfileManager = {
    // Abre el modal de editar perfil
    openEditProfileModal() {
        const modal = document.getElementById('editProfileModal');
        modal.classList.add('active');
        
        // Mostrar nombre de usuario
        document.getElementById('editProfileUsername').textContent = AppState.currentUser || '';
        
        // Actualizar avatar en el modal
        const avatar = document.getElementById('editProfileAvatar');
        const avatarText = document.getElementById('editProfileAvatarText');
        
        if (AppState.currentUserProfilePicture) {
            avatarText.style.display = 'none';
            let img = avatar.querySelector('img');
            if (!img) {
                img = document.createElement('img');
                avatar.insertBefore(img, avatar.firstChild);
            }
            img.src = AppState.currentUserProfilePicture;
        } else {
            const img = avatar.querySelector('img');
            if (img) img.remove();
            avatarText.style.display = 'block';
            avatarText.textContent = (AppState.currentUser || 'U')[0].toUpperCase();
        }
    },

    // Cierra el modal de editar perfil
    closeEditProfileModal() {
        document.getElementById('editProfileModal').classList.remove('active');
    },

    // Abre el selector de archivos para foto de perfil
    selectProfilePicture() {
        const input = document.createElement('input');
        input.type = 'file';
        input.accept = 'image/*';
        input.onchange = (e) => this.handleProfilePictureSelect(e);
        input.click();
    },

    // Maneja la selección de foto de perfil
    async handleProfilePictureSelect(event) {
        const file = event.target.files[0];
        if (!file) return;

        // Validar tamaño (máx 5MB para foto de perfil)
        const maxSize = 5 * 1024 * 1024;
        if (file.size > maxSize) {
            alert('La imagen es muy grande. Máximo: 5MB');
            return;
        }

        // Mostrar indicador de carga
        MediaManager.showUploadProgress();

        try {
            const url = await MediaManager.uploadToCloudinary(file);
            MediaManager.hideUploadProgress();
            
            // Enviar al servidor
            WebSocketManager.send({
                type: 'updateProfilePicture',
                profilePicture: url
            });

            // Actualizar UI local inmediatamente
            this.updateLocalAvatar(url);
            this.updateEditModalAvatar(url);
            AppState.currentUserProfilePicture = url;
        } catch (error) {
            MediaManager.hideUploadProgress();
            console.error('Error subiendo foto de perfil:', error);
            alert('Error al subir la foto. Intenta de nuevo.');
        }
    },

    // Actualiza el avatar en el modal de edición
    updateEditModalAvatar(url) {
        const avatar = document.getElementById('editProfileAvatar');
        const avatarText = document.getElementById('editProfileAvatarText');
        
        if (!avatar) return;
        
        if (url) {
            avatarText.style.display = 'none';
            let img = avatar.querySelector('img');
            if (!img) {
                img = document.createElement('img');
                avatar.insertBefore(img, avatar.firstChild);
            }
            img.src = url;
        }
    },

    // Actualiza el avatar local del usuario
    updateLocalAvatar(url) {
        const avatar = document.getElementById('profileAvatar');
        const avatarText = document.getElementById('profileAvatarText');
        
        if (url) {
            avatarText.style.display = 'none';
            let img = avatar.querySelector('img');
            if (!img) {
                img = document.createElement('img');
                avatar.insertBefore(img, avatar.firstChild);
            }
            img.src = url;
        } else {
            const img = avatar.querySelector('img');
            if (img) img.remove();
            avatarText.style.display = 'block';
        }
    },

    // Inicializa el avatar con la foto guardada
    initAvatar(username, profilePicture) {
        const avatarText = document.getElementById('profileAvatarText');
        avatarText.textContent = username[0].toUpperCase();
        
        if (profilePicture) {
            this.updateLocalAvatar(profilePicture);
            AppState.currentUserProfilePicture = profilePicture;
        }
    }
};

// ==================== GROUP MANAGER ====================
const GroupManager = {
    showCreateGroupModal() {
        const modal = document.getElementById('createGroupModal');
        const selection = document.getElementById('memberSelection');
        selection.innerHTML = '';
        
        AppState.contacts.forEach(contact => {
            const label = document.createElement('label');
            label.style.display = 'flex';
            label.style.alignItems = 'center';
            label.style.padding = '8px';
            label.style.cursor = 'pointer';
            
            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.value = contact.username;
            checkbox.style.marginRight = '10px';
            
            const text = document.createElement('span');
            text.textContent = contact.username;
            
            label.appendChild(checkbox);
            label.appendChild(text);
            selection.appendChild(label);
        });
        
        modal.classList.add('show');
    },

    closeCreateGroupModal() {
        document.getElementById('createGroupModal').classList.remove('show');
    },

    createGroup() {
        const name = document.getElementById('groupNameInput').value.trim();
        const checkboxes = document.querySelectorAll('#memberSelection input[type="checkbox"]:checked');
        const members = Array.from(checkboxes).map(cb => cb.value);
        
        if (!name || members.length === 0) {
            alert('Nombre del grupo y miembros necesarios');
            return;
        }
        
        WebSocketManager.send({
            type: 'createGroup',
            name: name,
            members: members
        });
        
        this.closeCreateGroupModal();
    }
};

// ==================== VIDEO CALL MANAGER ====================
const VideoCallManager = {
    // Quién es el peer remoto en la llamada actual
    callPeer: null,
    // ICE candidates recibidos antes de tener peerConnection
    pendingCandidates: [],

    // ========== Obtener credenciales TURN dinámicas ==========
    async getIceServers() {
        try {
            const url = `https://${CONFIG.METERED_DOMAIN}/api/v1/turn/credentials?apiKey=${CONFIG.METERED_API_KEY}`;
            const response = await fetch(url);
            if (response.ok) {
                const iceServers = await response.json();
                console.log('[WebRTC] Credenciales TURN dinámicas obtenidas:', iceServers.length, 'servidores');
                return { iceServers };
            }
            console.warn('[WebRTC] API Metered respondió con status:', response.status);
        } catch (err) {
            console.warn('[WebRTC] No se pudieron obtener credenciales TURN dinámicas:', err.message);
        }
        console.log('[WebRTC] Usando credenciales TURN estáticas');
        return CONFIG.ICE_SERVERS;
    },

    // ========== PASO 1: Quien llama inicia la solicitud ==========
    async initiateCall() {
        if (!AppState.activeChat || AppState.activeChatType === 'group') return;

        try {
            console.log('[CALL] Iniciando llamada a', AppState.activeChat);

            // Obtener media local
            AppState.localStream = await navigator.mediaDevices.getUserMedia({ audio: true, video: true });
            document.getElementById('localVideo').srcObject = AppState.localStream;
            console.log('[CALL] Media local obtenida');

            this.callPeer = AppState.activeChat;
            this.pendingCandidates = []; // Llamada nueva, limpiar candidatos viejos

            // Desbloquear remoteVideo para reproducción con audio (durante gesto de usuario)
            this.unlockRemoteAudio();

            // Obtener credenciales TURN
            const iceConfig = await this.getIceServers();
            this.setupPeerConnection(iceConfig);

            // Crear offer
            const offer = await AppState.peerConnection.createOffer();
            await AppState.peerConnection.setLocalDescription(offer);
            console.log('[CALL] Offer creado, SDP length:', offer.sdp.length);

            // Enviar solicitud con el offer (serializado explícitamente)
            WebSocketManager.send({
                type: 'callRequest',
                from: AppState.currentUser,
                to: AppState.activeChat,
                offer: { type: offer.type, sdp: offer.sdp }
            });
            console.log('[CALL] callRequest enviado a', AppState.activeChat);

            // Mostrar modal
            this.showVideoModal(AppState.activeChat, 'Llamando...');
        } catch (error) {
            console.error('[CALL] Error al iniciar llamada:', error);
            alert('No se pudo acceder a la cámara/micrófono.');
            this.cleanup();
        }
    },

    // ========== PASO 2: Quien recibe ve la notificación ==========
    handleCallRequest(data) {
        console.log('[CALL] callRequest recibido de', data.from, '- offer presente:', !!data.offer);
        AppState.incomingCallData = data;
        this.callPeer = data.from;
        const initial = data.from ? data.from.charAt(0).toUpperCase() : '?';
        document.getElementById('incomingCallAvatar').textContent = initial;
        document.getElementById('callNotificationText').textContent = data.from;
        document.getElementById('callNotification').classList.add('show');
    },

    // ========== PASO 3: Quien recibe acepta ==========
    async acceptCall() {
        document.getElementById('callNotification').classList.remove('show');
        const data = AppState.incomingCallData;
        if (!data) return;

        try {
            console.log('[CALL] Aceptando llamada de', data.from);

            // Obtener media local
            AppState.localStream = await navigator.mediaDevices.getUserMedia({ audio: true, video: true });
            document.getElementById('localVideo').srcObject = AppState.localStream;
            console.log('[CALL] Media local obtenida (receptor)');

            this.callPeer = data.from;
            // NO limpiar pendingCandidates aquí — pueden tener ICE candidates del llamante

            // Desbloquear remoteVideo para reproducción con audio (durante gesto de usuario)
            this.unlockRemoteAudio();

            // Obtener credenciales TURN
            const iceConfig = await this.getIceServers();
            this.setupPeerConnection(iceConfig);

            // Establecer el offer remoto
            console.log('[CALL] Estableciendo remote description (offer), type:', data.offer?.type);
            await AppState.peerConnection.setRemoteDescription(new RTCSessionDescription(data.offer));
            console.log('[CALL] Remote description establecida');

            // Aplicar ICE candidates pendientes
            console.log('[CALL] Flushing', this.pendingCandidates.length, 'ICE candidates pendientes');
            this.flushPendingCandidates();

            // Crear answer
            const answer = await AppState.peerConnection.createAnswer();
            await AppState.peerConnection.setLocalDescription(answer);
            console.log('[CALL] Answer creado, SDP length:', answer.sdp.length);

            // Enviar answer (serializado explícitamente)
            WebSocketManager.send({
                type: 'callAnswer',
                to: data.from,
                answer: { type: answer.type, sdp: answer.sdp }
            });
            console.log('[CALL] callAnswer enviado a', data.from);

            this.showVideoModal(data.from, 'Conectando...');
        } catch (error) {
            console.error('[CALL] Error al aceptar llamada:', error);
            alert('No se pudo acceder a la cámara/micrófono.');
            this.cleanup();
        }
    },

    // ========== PASO 4: Quien llamó recibe el answer ==========
    async handleCallAnswer(data) {
        try {
            if (!AppState.peerConnection) {
                console.warn('[CALL] handleCallAnswer: No hay peerConnection!');
                return;
            }
            console.log('[CALL] callAnswer recibido, answer type:', data.answer?.type);
            await AppState.peerConnection.setRemoteDescription(new RTCSessionDescription(data.answer));
            console.log('[CALL] Remote description (answer) establecida');

            // Aplicar ICE candidates pendientes
            console.log('[CALL] Flushing', this.pendingCandidates.length, 'ICE candidates pendientes (caller)');
            this.flushPendingCandidates();

            document.getElementById('callStatusText').textContent = 'Conectado';
            document.getElementById('callDuration').textContent = 'Conectado';
        } catch (error) {
            console.error('[CALL] Error al recibir answer:', error);
        }
    },

    // ========== Rechazar llamada ==========
    rejectCall() {
        document.getElementById('callNotification').classList.remove('show');
        if (AppState.incomingCallData) {
            WebSocketManager.send({
                type: 'callRejected',
                to: AppState.incomingCallData.from
            });
        }
        AppState.incomingCallData = null;
        this.callPeer = null;
    },

    // ========== Configurar PeerConnection ==========
    setupPeerConnection(iceConfig) {
        if (AppState.peerConnection) {
            AppState.peerConnection.close();
        }
        // NO limpiar pendingCandidates aquí: pueden tener ICE del otro peer

        const config = iceConfig || CONFIG.ICE_SERVERS;
        console.log('[WebRTC] Creando PeerConnection con', config.iceServers?.length, 'servidores ICE');
        AppState.peerConnection = new RTCPeerConnection(config);

        // Agregar tracks locales
        if (AppState.localStream) {
            AppState.localStream.getTracks().forEach(track => {
                AppState.peerConnection.addTrack(track, AppState.localStream);
            });
        }

        // Recibir tracks remotos
        AppState.peerConnection.ontrack = (event) => {
            console.log('[WebRTC] ontrack fired, streams:', event.streams.length, 'track kind:', event.track?.kind);
            const remoteVideo = document.getElementById('remoteVideo');
            if (remoteVideo && event.streams[0]) {
                remoteVideo.srcObject = event.streams[0];
                // play() ya fue desbloqueado con unlockRemoteAudio() durante el gesto de usuario
                remoteVideo.play().then(() => {
                    console.log('[WebRTC] Remote video+audio playing (unmuted)');
                    this.onRemoteVideoConnected();
                }).catch(err => {
                    console.warn('[WebRTC] Remote video play() error:', err.name, err.message);
                    // Solo mutear temporalmente como último recurso, y desmutear al tocar
                    remoteVideo.muted = true;
                    remoteVideo.play().then(() => {
                        console.log('[WebRTC] Remote video playing MUTED — esperando interacción para desmutear');
                        this.onRemoteVideoConnected();
                        // Auto-desmutear cuando el usuario toque la pantalla
                        this.setupAutoUnmute();
                    }).catch(e => console.error('[WebRTC] Fatal play error:', e));
                });
            } else if (event.track) {
                // Fallback: si no hay streams, crear uno
                console.log('[WebRTC] No streams en ontrack, usando track directamente:', event.track.kind);
                if (!remoteVideo.srcObject) {
                    const stream = new MediaStream();
                    remoteVideo.srcObject = stream;
                }
                remoteVideo.srcObject.addTrack(event.track);
                remoteVideo.play().catch(() => {});
            }
        };

        // Enviar ICE candidates al peer (serializado explícitamente)
        AppState.peerConnection.onicecandidate = (event) => {
            if (event.candidate && this.callPeer) {
                console.log('[WebRTC] ICE candidate generado, enviando a', this.callPeer);
                WebSocketManager.send({
                    type: 'iceCandidate',
                    to: this.callPeer,
                    candidate: {
                        candidate: event.candidate.candidate,
                        sdpMid: event.candidate.sdpMid,
                        sdpMLineIndex: event.candidate.sdpMLineIndex,
                        usernameFragment: event.candidate.usernameFragment
                    }
                });
            } else if (!event.candidate) {
                console.log('[WebRTC] ICE gathering completado');
            }
        };

        // Monitorear estado de conexión
        AppState.peerConnection.onconnectionstatechange = () => {
            const state = AppState.peerConnection?.connectionState;
            console.log('[WebRTC] Connection state:', state);
            if (state === 'connected') {
                this.onRemoteVideoConnected();
            } else if (state === 'failed') {
                console.error('[WebRTC] Conexión fallida');
                document.getElementById('callStatusText').textContent = 'Conexión fallida';
                document.getElementById('callDuration').textContent = 'Error';
            } else if (state === 'disconnected') {
                console.warn('[WebRTC] Conexión perdida');
            }
        };

        AppState.peerConnection.oniceconnectionstatechange = () => {
            const iceState = AppState.peerConnection?.iceConnectionState;
            console.log('[WebRTC] ICE state:', iceState);
            if (iceState === 'connected' || iceState === 'completed') {
                this.onRemoteVideoConnected();
            } else if (iceState === 'failed') {
                console.error('[WebRTC] ICE connection FAILED - posible problema con TURN/NAT');
                document.getElementById('callStatusText').textContent = 'Error de conexión';
            }
        };

        AppState.peerConnection.onicegatheringstatechange = () => {
            console.log('[WebRTC] ICE gathering state:', AppState.peerConnection?.iceGatheringState);
        };

        AppState.peerConnection.onsignalingstatechange = () => {
            console.log('[WebRTC] Signaling state:', AppState.peerConnection?.signalingState);
        };
    },

    // ========== handleCallOffer (legacy, por si el server reenvía como callOffer) ==========
    async handleCallOffer(data) {
        console.log('[CALL] callOffer recibido de', data.from);
        if (!AppState.peerConnection) {
            this.handleCallRequest(data);
            return;
        }
        try {
            await AppState.peerConnection.setRemoteDescription(new RTCSessionDescription(data.offer));
            this.flushPendingCandidates();
            const answer = await AppState.peerConnection.createAnswer();
            await AppState.peerConnection.setLocalDescription(answer);
            WebSocketManager.send({
                type: 'callAnswer',
                to: data.from,
                answer: { type: answer.type, sdp: answer.sdp }
            });
        } catch (error) {
            console.error('[CALL] Error handling call offer:', error);
        }
    },

    // ========== ICE Candidates ==========
    handleIceCandidate(data) {
        console.log('[WebRTC] ICE candidate recibido de', data.from || 'peer', '- PC:', !!AppState.peerConnection, '- RD:', !!AppState.peerConnection?.remoteDescription);
        if (AppState.peerConnection && AppState.peerConnection.remoteDescription) {
            AppState.peerConnection.addIceCandidate(new RTCIceCandidate(data.candidate)).then(() => {
                console.log('[WebRTC] ICE candidate añadido correctamente');
            }).catch(err => {
                console.error('[WebRTC] Error adding ICE candidate:', err);
            });
        } else {
            console.log('[WebRTC] ICE candidate encolado (esperando remoteDescription)');
            this.pendingCandidates.push(data.candidate);
        }
    },

    flushPendingCandidates() {
        if (!AppState.peerConnection) return;
        const count = this.pendingCandidates.length;
        console.log('[WebRTC] Aplicando', count, 'ICE candidates pendientes');
        this.pendingCandidates.forEach(candidate => {
            AppState.peerConnection.addIceCandidate(new RTCIceCandidate(candidate)).catch(err => {
                console.error('[WebRTC] Error adding queued ICE candidate:', err);
            });
        });
        this.pendingCandidates = [];
    },

    // ========== Terminar llamada ==========
    endCall() {
        const peer = this.callPeer;
        this.cleanup();

        if (peer) {
            WebSocketManager.send({
                type: 'callEnded',
                to: peer
            });
        }
    },

    handleCallEnded() {
        this.cleanup();
    },

    handleCallRejected() {
        this.cleanup();
        alert('La llamada fue rechazada');
    },

    cleanup() {
        if (AppState.peerConnection) {
            AppState.peerConnection.close();
            AppState.peerConnection = null;
        }

        if (AppState.localStream) {
            AppState.localStream.getTracks().forEach(track => track.stop());
            AppState.localStream = null;
        }

        if (AppState.callDurationInterval) {
            clearInterval(AppState.callDurationInterval);
            AppState.callDurationInterval = null;
        }

        this.callPeer = null;
        this.pendingCandidates = [];
        this._autoUnmuteSet = false;
        AppState.incomingCallData = null;
        AppState.audioEnabled = true;
        AppState.videoEnabled = true;

        const videoModal = document.getElementById('videoCallModal');
        if (videoModal) videoModal.classList.remove('show');

        const notification = document.getElementById('callNotification');
        if (notification) notification.classList.remove('show');

        // Reset overlay
        const overlay = document.getElementById('callConnectingOverlay');
        if (overlay) overlay.classList.remove('connected');
    },

    // Desbloquear el elemento remoteVideo para que pueda reproducir audio
    // DEBE llamarse durante un gesto de usuario (click en llamar/aceptar)
    unlockRemoteAudio() {
        const remoteVideo = document.getElementById('remoteVideo');
        if (remoteVideo) {
            remoteVideo.muted = false;
            // Crear un stream vacío temporal para "desbloquear" el elemento
            const silentCtx = new (window.AudioContext || window.webkitAudioContext)();
            const silentDest = silentCtx.createMediaStreamDestination();
            remoteVideo.srcObject = silentDest.stream;
            remoteVideo.play().then(() => {
                console.log('[AUDIO] remoteVideo desbloqueado para audio con gesto de usuario');
                remoteVideo.srcObject = null; // limpiar, se asignará el stream real en ontrack
                silentCtx.close();
            }).catch(err => {
                console.warn('[AUDIO] No se pudo desbloquear remoteVideo:', err.message);
                silentCtx.close();
            });
        }
    },

    // Si el browser forzó muted, desmutear al primer toque/click
    setupAutoUnmute() {
        if (this._autoUnmuteSet) return;
        this._autoUnmuteSet = true;
        const handler = () => {
            const remoteVideo = document.getElementById('remoteVideo');
            if (remoteVideo && remoteVideo.muted) {
                remoteVideo.muted = false;
                console.log('[AUDIO] remoteVideo desmuteado por interacción de usuario');
            }
            this._autoUnmuteSet = false;
            document.getElementById('videoCallModal').removeEventListener('click', handler);
            document.getElementById('videoCallModal').removeEventListener('touchstart', handler);
        };
        document.getElementById('videoCallModal').addEventListener('click', handler);
        document.getElementById('videoCallModal').addEventListener('touchstart', handler);
    },

    // Llamado cuando el video remoto se conecta (desde múltiples triggers)
    onRemoteVideoConnected() {
        const overlay = document.getElementById('callConnectingOverlay');
        if (overlay && !overlay.classList.contains('connected')) {
            console.log('[WebRTC] Ocultando overlay de conexión');
            overlay.classList.add('connected');
            this.startCallTimer();
        }
    },

    // ========== UI ==========
    showVideoModal(peerName, statusText) {
        const initial = peerName ? peerName.charAt(0).toUpperCase() : '?';

        // Overlay de conexión
        const overlay = document.getElementById('callConnectingOverlay');
        if (overlay) overlay.classList.remove('connected');

        document.getElementById('callAvatar').textContent = initial;
        document.getElementById('callPeerName').textContent = peerName || 'Contacto';
        document.getElementById('callStatusText').textContent = statusText || 'Conectando...';

        // Info bar superior
        document.getElementById('callInfoAvatar').textContent = initial;
        document.getElementById('callHeader').textContent = peerName || 'Videollamada';
        document.getElementById('callDuration').textContent = statusText || 'Conectando...';

        // Mostrar modal
        document.getElementById('videoCallModal').classList.add('show');

        // Múltiples listeners por si ontrack no es suficiente
        const remoteVideo = document.getElementById('remoteVideo');
        remoteVideo.onplaying = () => this.onRemoteVideoConnected();
        remoteVideo.onloadedmetadata = () => {
            console.log('[WebRTC] Remote video metadata loaded');
            remoteVideo.play().catch(() => {});
        };
    },

    startCallTimer() {
        if (AppState.callDurationInterval) return; // ya corriendo
        AppState.callStartTime = Date.now();

        AppState.callDurationInterval = setInterval(() => {
            const duration = Math.floor((Date.now() - AppState.callStartTime) / 1000);
            const minutes = Math.floor(duration / 60);
            const seconds = duration % 60;
            document.getElementById('callDuration').textContent = `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
        }, 1000);
    },

    toggleAudio() {
        AppState.audioEnabled = !AppState.audioEnabled;
        if (AppState.localStream) {
            AppState.localStream.getAudioTracks().forEach(track => {
                track.enabled = AppState.audioEnabled;
            });
        }
        document.getElementById('audioBtn').classList.toggle('muted', !AppState.audioEnabled);
    },

    toggleVideo() {
        AppState.videoEnabled = !AppState.videoEnabled;
        if (AppState.localStream) {
            AppState.localStream.getVideoTracks().forEach(track => {
                track.enabled = AppState.videoEnabled;
            });
        }
        document.getElementById('videoBtn').classList.toggle('muted', !AppState.videoEnabled);
    }
};

// ==================== INITIALIZATION ====================
function initializeApp() {
    AuthManager.init();
    UIManager.initEmojiPicker();
    WebSocketManager.connect();
    UIManager.showAuth();
}

// Global functions for HTML onclick handlers
window.switchTab = (tab) => AuthManager.switchTab(tab);
window.login = () => AuthManager.login();
window.register = () => AuthManager.register();
window.logout = () => AuthManager.logout();
window.switchView = (view) => UIManager.switchView(view);
window.filterContacts = () => UIManager.filterContacts();
window.toggleEmojiPicker = () => UIManager.toggleEmojiPicker();
window.showImageModal = (src) => UIManager.showImageModal(src);
window.closeImageModal = () => UIManager.closeImageModal();
window.showCreateGroupModal = () => GroupManager.showCreateGroupModal();
window.closeCreateGroupModal = () => GroupManager.closeCreateGroupModal();
window.createGroup = () => GroupManager.createGroup();
window.initiateCall = () => VideoCallManager.initiateCall();
window.acceptCall = () => VideoCallManager.acceptCall();
window.rejectCall = () => VideoCallManager.rejectCall();
window.endCall = () => VideoCallManager.endCall();
window.toggleAudio = () => VideoCallManager.toggleAudio();
window.toggleVideo = () => VideoCallManager.toggleVideo();
window.ProfileManager = ProfileManager;

// Start the app when DOM is loaded
document.addEventListener('DOMContentLoaded', initializeApp);
